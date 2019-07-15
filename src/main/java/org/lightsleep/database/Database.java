// Database.java
// (C) 2016 Masato Kokubo

package org.lightsleep.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.lightsleep.RuntimeSQLException;
import org.lightsleep.Sql;
import org.lightsleep.helper.TypeConverter;
import org.lightsleep.helper.Utils;

/**
 * An interface to generate SQLs.
 *
 * @since 1.0.0
 * @author Masato Kokubo
 */
public interface Database {
	/**
	 * Returns whether support <b>OFFSET</b> and <b>LIMIT</b> in the SELECT SQL.
	 *
	 * @return <b>true</b> if support <b>OFFSET</b> and <b>LIMIT</b>, <b>false</b> otherwise
	 */
	default boolean supportsOffsetLimit() {
		return false;
	}

	/**
	 * Creates and returns a SELECT SQL.
	 *
	 * @param <E> the type of the entity
	 * @param sql a <b>Sql</b> object
	 * @param parameters a list to add the parameters of the SQL
	 * @return a SELECT SQL string
	 *
	 * @throws IllegalStateException if SELECT SQL without columns was generated
	 */
	 <E> String selectSql(Sql<E> sql, List<Object> parameters);

	/**
	 * Creates and returns a SELECT SQL excluding
	 * <b>OFFSET</b>/<b>LIMIT</b>,
	 * <b>FOR UPDATE</b> and
	 * <b>ORDER BY</b>.
	 *
	 * @param <E> the type of the entity
	 * @param sql a <b>Sql</b> object
	 * @param parameters a list to add the parameters of the SQL
	 * @return a SELECT SQL string
	 *
	 * @throws IllegalStateException if SELECT SQL without columns was generated
	 */
	 <E> String subSelectSql(Sql<E> sql, List<Object> parameters);

	/**
	 * Creates and returns a SELECT SQL excluding
	 * <b>OFFSET</b>/<b>LIMIT</b>,
	 * <b>FOR UPDATE</b> and
	 * <b>ORDER BY</b>.
	 *
	 * @param <E> the type of the entity
	 * @param sql a <b>Sql</b> object
	 * @param columnsSupplier a Supplier of the columns string
	 * @param parameters a list to add the parameters of the SQL
	 * @return a SELECT SQL string
	 */
	 <E> String subSelectSql(Sql<E> sql, Supplier<CharSequence> columnsSupplier, List<Object> parameters);

	/**
	 * Creates and returns a INSERT SQL.
	 *
	 * @param <E> the type of the entity
	 * @param sql a <b>Sql</b> object
	 * @param parameters a list to add the parameters of the SQL
	 * @return a INSERT SQL string
	 */
	 <E> String insertSql(Sql<E> sql, List<Object> parameters);

	/**
	 * Creates and returns a UPDATE SQL.
	 *
	 * @param <E> the type of the entity
	 * @param sql a <b>Sql</b> object
	 * @param parameters a list to add the parameters of the SQL
	 * @return a INSERT SQL string
	 */
	 <E> String updateSql(Sql<E> sql, List<Object> parameters);

	/**
	 * Creates and returns a DELETE SQL.
	 *
	 * @param <E> the type of the entity
	 * @param sql a <b>Sql</b> object
	 * @param parameters a list to add the parameters of the SQL
	 * @return a INSERT SQL string
	 */
	 <E> String deleteSql(Sql<E> sql, List<Object> parameters);

	/**
	 * Returns the <b>TypeConverter</b> map.
	 *
	 * @return the <b>TypeConverter</b> map
	 */
	Map<String, TypeConverter<?, ?>> typeConverterMap();

	/**
	 * Converts the object to the specified type.
	 *
	 * @param <T> the destination type
	 * @param value an object to be converted
	 * @param type the class object of the destination type
	 * @return a converted object
	 */
	 <T> T convert(Object value, Class<T> type);

	/**
	 * Masks ths password of the JDBC URL.
	 *
	 * @param jdbcUrl a JDBC URL
	 * @return the JDBC URL masked the password
	 *
	 * @since 2.2.0
	 */
// 3.0.0
//	String maskPassword(String jdbcUrl);
	default String maskPassword(String jdbcUrl) {
		return SQLServer.instance.maskPassword(MySQL.instance.maskPassword(jdbcUrl));
	}
////

	/**
	 * Gets the value from the resultSet and returns it.
	 *
	 * @param connection the <b>Connection</b> object
	 * @param resultSet the <b>ResultSet</b> object
	 * @param columnLabel the label for the column
	 * @return the column value
	 *
	 * @throws NullPointerException if <b>connection</b>, <b>resultSet</b> or <b>columnLabel</b> is <b>null</b>
	 * @throws RuntimeSQLException if a <b>SQLException</b> is thrown while accessing the database, replaces it with this exception
	 *
	 * @since 3.0.0
	 */
	default Object getObject(Connection connection, ResultSet resultSet, String columnLabel) {
	// 3.1.0
	//	Objects.requireNonNull(connection, "connection");
	//	Objects.requireNonNull(resultSet, "resultSet");
	//	Objects.requireNonNull(columnLabel, "columnLabel");
	////
		try {
			Object object = resultSet.getObject(columnLabel);

			if (Standard.logger.isDebugEnabled())
				Standard.logger.debug("Database.getObject: columnLabel: " + columnLabel
					+ ", getted object: " + Utils.toLogString(object));

			return object;
		}
		catch (SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}

	/**
	 * Returns a database handler related to <b>jdbcUrl</b>.
	 *
	 * @param jdbcUrl a JDBC URL
	 * @return the database handler related to the JDBC URL
	 *
	 * @throws IllegalArgumentException if <b>jdbcUrl</b> does not contain a string that can identify a <b>Database</b> class
	 *
	 * @since 2.1.0
	 */
	@SuppressWarnings("unchecked")
	static Database getInstance(String jdbcUrl) {
		Objects.requireNonNull(jdbcUrl, "jdbcUrl");
		String[] words = jdbcUrl.split(":");
		for (String word : words) {
			if (word.equals("jdbc")) continue;
			if (!word.matches("[a-z0-9]+")) continue;

			Class<? extends Database> anchorClass;
			try {
				String anchorClassName = Database.class.getPackage().getName() + ".anchor." + word;
				anchorClass = (Class<? extends Database>)Class.forName(anchorClassName);
			} catch (ClassNotFoundException e) {
				continue;
			}

			try {
				Class<? extends Database> databaseClass = (Class<? extends Database>)anchorClass.getSuperclass();
				Database database = (Database)databaseClass.getField("instance").get(null);
				return database;

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		throw new IllegalArgumentException(jdbcUrl);
	}
}
