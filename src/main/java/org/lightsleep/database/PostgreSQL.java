// PostgreSQL.java
// (C) 2016 Masato Kokubo

package org.lightsleep.database;

import org.lightsleep.component.SqlString;
import org.lightsleep.helper.TypeConverter;

//*   <tr><td>boolean</td><td rowspan="3">{@linkplain org.lightsleep.component.SqlString}</td><td>FALSE or TRUE</td></tr>
/**
 * A database handler for
 * <a href="http://www.postgresql.org/" target="PostgreSQL">PostgreSQL</a>.<br>
 *
 * The object of this class has a <b>TypeConverter</b> map
 * with the following additional <b>TypeConverter</b> to
 * {@linkplain Standard#typeConverterMap}.

 * <table class="additional">
 *   <caption><span>Registered TypeConverter objects</span></caption>
 *   <tr><th>Source data type</th><th>Destination data type</th><th>Conversion Format</th></tr>
 *   <tr><td>String </td><td rowspan="2">{@linkplain org.lightsleep.component.SqlString}</td>
	                     <td><i>sql parameter (?)</i> if too long, '...' (may include escape sequences) otherwise</td></tr>
 *   <tr><td>byte[] </td><td><i>sql parameter (?)</i> if too long, E'\\x...' otherwise</td></tr>
 * </table>
 *
 * @since 1.0.0
 * @author Masato Kokubo
 * @see org.lightsleep.helper.TypeConverter
 * @see org.lightsleep.database.Standard
 */
public class PostgreSQL extends Standard {
	// The PostgreSQL instance
	private static final Database instance = new PostgreSQL();

	/**
	 * Returns the <b>PostgreSQL</b> instance.
	 *
	 * @return the <b>PostgreSQL</b> instance
	 */
	public static Database instance() {
		return instance;
	}

	/**
	 * Constructs a new <b>PostgreSQL</b>.
	 */
	protected PostgreSQL() {
	// 1.8.0
	//	/** boolean -> FALSE, TRUE */
	//	TypeConverter.put(typeConverterMap, booleanToSqlFalseTrueConverter);
	////

		// String.class -> SqlString.class
		TypeConverter.put(typeConverterMap,
			new TypeConverter<>(String.class, SqlString.class, object -> {
				if (object.length() > maxStringLiteralLength)
				// 1.7.0
				//	return SqlString.PARAMETER; // SQL Paramter
					return new SqlString(SqlString.PARAMETER, object); // SQL Paramter
				////

				boolean escaped = false;
				StringBuilder buff = new StringBuilder(object.length() + 2);
				buff.append('\'');
				char[] chars = object.toCharArray();
			// 1.7.0
			//	for (int index = 0; index < chars.length; ++index) {
			//		char ch = chars[index];
				for (char ch : chars) {
			////
					switch (ch) {
					case '\b'    : buff.append("\\b" ); escaped = true; break; // 07 BEL
					case '\t'    : buff.append("\\t" ); escaped = true; break; // 09 HT
					case '\n'    : buff.append("\\n" ); escaped = true; break; // 0A LF
					case '\f'    : buff.append("\\f" ); escaped = true; break; // 0C FF
					case '\r'    : buff.append("\\r" ); escaped = true; break; // 0D CR
				// 1.2.0
				//	case '\''    : buff.append("''"  ); escaped = true; break;
					case '\''    : buff.append("''"  ); break;
				////
					case '\\'    : buff.append("\\\\"); escaped = true; break;
					default      : buff.append(ch    ); break;
					}
				}
				buff.append('\'');
				String string = buff.toString();
				if (escaped)
					string = 'E' + string;
				return new SqlString(string);
			})
		);

		// byte[].class -> SqlString.class
		TypeConverter.put(typeConverterMap,
			new TypeConverter<>(byte[].class, SqlString.class, object -> {
				if (object.length > maxBinaryLiteralLength)
				// 1.7.0
				//	return SqlString.PARAMETER; // SQL Paramter
					return new SqlString(SqlString.PARAMETER, object); // SQL Paramter
				////

				StringBuilder buff = new StringBuilder(object.length * 2 + 5);
				buff.append("E'\\\\x");
			// 1.7.0
			//	for (int b : object) {
			//		if (b < 0) b += 256;
			//		char ch = (char)('0' + b / 16);
			//		if (ch > '9') ch += 'A' - ('9' + 1);
			//		buff.append(ch);
			//
			//		ch = (char)('0' + b % 16);
			//		if (ch > '9') ch += 'A' - ('9' + 1);
			//		buff.append(ch);
			//	}
			//
			//	buff.append("'");
				for (int value : object) {
					value &= 0xFF;
					char ch = (char)((value >>> 4) + '0');
					if (ch > '9') ch += 'A' - ('9' + 1);
					buff.append(ch);
					ch = (char)((value & 0x0F) + '0');
					if (ch > '9') ch += 'A' - ('9' + 1);
					buff.append(ch);
				}
				buff.append('\'');
			////
				return new SqlString(buff.toString());
			})
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsOffsetLimit() {
		return true;
	}
}
