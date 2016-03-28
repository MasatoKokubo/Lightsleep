/*
	Standard.java
	Copyright (c) 2016 Masato Kokubo
*/
package org.mkokubo.lightsleep.database;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.mkokubo.lightsleep.component.SqlString;
import org.mkokubo.lightsleep.helper.TypeConverter;
import org.mkokubo.lightsleep.Sql;

/**
	特定の DBMS に依存しないデータベース・ハンドラーです。<br>

	このクラスのオブジェクトは、
	{@linkplain org.mkokubo.lightsleep.helper.TypeConverter#typeConverterMap}
	に以下の <b>TypeConverter</b> を追加した <b>TypeConverter</b> マップを持ちます。

	<table class="additinal">
		<caption>登録されている {@code TypeConverter} オブジェクト</caption>
		<tr><th>変換元データ型</th><th>変換先データ型</th></tr>
		<tr><td>boolean       </td><td>{@linkplain org.mkokubo.lightsleep.component.SqlString} (FALSE, TRUE)</td></tr>
		<tr><td>Byte          </td><td rowspan="15">{@linkplain org.mkokubo.lightsleep.component.SqlString}</td></tr>
		<tr><td>Short         </td></tr>
		<tr><td>Integer       </td></tr>
		<tr><td>Long          </td></tr>
		<tr><td>Float         </td></tr>
		<tr><td>Double        </td></tr>
		<tr><td>BigInteger    </td></tr>
		<tr><td>BigDecimal    </td></tr>
		<tr><td>String        </td></tr>
		<tr><td>Character     </td></tr>
		<tr><td>java.sql.Date </td></tr>
		<tr><td>Time          </td></tr>
		<tr><td>Timestamp     </td></tr>
		<tr><td>byte[]        </td></tr>
		<tr><td>Enum          </td></tr>
	</table>

	@since 1.0.0
	@author Masato Kokubo
*/
public class Standard implements Database {
	/**
		SQL が作成される時の文字列リテラルの最大長。
		文字列リテラルがこの長さを超える場合、SQL のパラメータ (?) として生成します。<br>
		lightsleep.properties の <b>maxStringLiteralLength</b>
		の値が設定されます。(未定義の場合は 128)
	*/
	protected static final int maxStringLiteralLength = 0;

	/**
		SQL が作成される時のバイナリ列リテラルの最大長。
		バイナリ列リテラルがこの長さを超える場合、SQL のパラメータ (?) として生成します。<br>
		lightsleep.properties の <b>maxBinaryLiteralLength</b>
		の値が設定されます。(未定義の場合は 128)
	*/
	protected static final int maxBinaryLiteralLength = 0;

	/**
		<b>boolean</b> から <b>SqlString</b> (FALSE, TRUE) へ変換する
		<b>TypeConverter</b> オブジェクト
	*/
	public static final TypeConverter<Boolean, SqlString> booleanToSqlFalseTrueConverter = null;

	/**
		<b>boolean</b> から <b>SqlString</b> (0, 1) へ変換する
		<b>TypeConverter</b> オブジェクト
	*/
	public static final TypeConverter<Boolean, SqlString> booleanToSql01Converter = null;

	/**
		<b>boolean</b> から <b>SqlString</b> ('0', '1') へ変換する
		<b>TypeConverter</b> オブジェクト
	*/
	public static final TypeConverter<Boolean, SqlString> booleanToSqlChar01Converter = null;

	/**
		<b>boolean</b> から <b>SqlString</b> ('N', 'Y') へ変換する
		<b>TypeConverter</b> オブジェクト
	*/
	public static final TypeConverter<Boolean, SqlString> booleanToSqlNYConverter = null;

	/**
		<b>String</b> ("N", "Y") から <b>boolean</b> へ変換する
		<b>TypeConverter</b> オブジェクト
	*/
	public static final TypeConverter<String, Boolean> stringNYToBooleanConverter = null;

	/**
		<b>Standard</b> オブジェクトを返します。

		@return Standard オブジェクト
	*/
	public static Database instance() {
		return null;
	}

	/**
		<b>Standard</b> を構築します。
	*/
	protected Standard() {
	}

	/**
		以下のデータ型変換で使用する <b>TypeConverter</b> マップ<br>
		<ul>
			<li>SQL 生成時</li>
			<li>SELECT SQL で取得した値をエンティティに格納する際</li>
		</ul>
	*/
	protected final Map<String, TypeConverter<?, ?>> typeConverterMap = null;

	/**
		{@inheritDoc}
	*/
	@Override
	public <E> String selectSql(Sql<E> sql, List<Object> parameters) {
		return null;
	}

	/**
		{@inheritDoc}
	*/
	@Override
	public <E> String subSelectSql(Sql<E> sql, List<Object> parameters) {
		return null;
	}

	/**
		{@inheritDoc}
	*/
	@Override
	public <E> String subSelectSql(Sql<E> sql, Supplier<CharSequence> columnsSupplier, List<Object> parameters) {
		return null;
	}

	/**
		{@inheritDoc}
	*/
	@Override
	public <E> String insertSql(Sql<E> sql, List<Object> parameters) {
		return null;
	}

	/**
		{@inheritDoc}
	*/
	@Override
	public <E> String updateSql(Sql<E> sql, List<Object> parameters) {
		return null;
	}

	/**
		{@inheritDoc}
	*/
	@Override
	public <E> String deleteSql(Sql<E> sql, List<Object> parameters) {
		return null;
	}

	/**
		{@inheritDoc}
	*/
	@Override
	public Map<String, TypeConverter<?, ?>> typeConverterMap() {
		return null;
	}

	/**
		{@inheritDoc}
	*/
	@Override
	public <T> T convert(Object value, Class<T> type) {
		return null;
	}
}