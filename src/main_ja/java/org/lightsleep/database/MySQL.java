// MySQL.java
// (C) 2016 Masato Kokubo

package org.lightsleep.database;

import org.lightsleep.Sql;

/**
 * <a href="http://www.mysql.com/" target="MySQL">MySQL</a>
 * 用のデータベース・ハンドラーです。
 *
 * <p>
 * このクラスのオブジェクトは、{@linkplain Standard#typeConverterMap}
 * に以下の <b>TypeConverter</b> オブジェクトを追加した<b>TypeConverter</b> マップを持ちます。
 * </p>
 *
 * <table class="additional">
 *   <caption><span>追加される TypeConverter オブジェクト</span></caption>
 *   <tr><th>変換元データ型</th><th>変換先データ型</th><th>変換内容</th></tr>
 *   <tr><td>Boolean</td><td rowspan="2">SqlString</td><td>false ➔ <code>0</code><br>true ➔ <code>1</code></td></tr>
 *   <tr><td>String </td><td><code>'...'</code><br>制御文字はエスケープ・シーケンスに変換<br>長い文字列の場合は <code>?</code> <i>(SQLパラメータ)</i></td></tr>
 * </table>
 *
 * @since 1.0.0
 * @author Masato Kokubo
 * @see org.lightsleep.helper.TypeConverter
 * @see org.lightsleep.database.Standard
 */
public class MySQL extends Standard {
	/**
	 * <b>MySQL</b> オブジェクトを返します。
	 *
	 * @return MySQL オブジェクト
	 */
	public static Database instance() {
		return null;
	}

	/**
	 * <b>MySQL</b> を構築します。
	 */
	protected MySQL() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supportsOffsetLimit() {
		return true;
	}
}
