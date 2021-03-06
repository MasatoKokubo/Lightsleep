// MariaDB.java
// (C) 2016 Masato Kokubo

package org.lightsleep.database;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * <a href="https://mariadb.org/" target="MariaDB">MariaDB</a>
 * 用のデータベースハンドラです。
 *
 * <p>
 * このクラスのオブジェクトは、{@linkplain Standard#typeConverterMap}
 * に以下の<b>TypeConverter</b>オブジェクトを追加した<b>TypeConverter</b>マップを持ちます。
 * </p>
 *
 * <table class="additional">
 *   <caption><span>TypeConverterマップへの追加内容</span></caption>
 *   <tr><th colspan="2">キー: データ型</th><th rowspan="2">値: 変換関数</th></tr>
 *   <tr><th>変換元</th><th>変換先</th></tr>
 *
 *   <tr><td>Boolean</td><td rowspan="2">SqlString</td>
 *     <td>
 *       <b>new SqlString("0")</b> <span class="comment">変換元の値が<b>false</b>の場合</span><br>
 *       <b>new SqlString("1")</b> <span class="comment">変換元の値が<b>true</b>の場合</span>
 *     </td>
 *   </tr>
 *   <tr><td>String</td>
 *     <td>
 *       <b>new SqlString("'" + source + "'")</b><br>
 *       <span class="comment">変換元の文字列中のシングルクォートは、連続する2個のシングルクォートに変換、<br>
 *       また制御文字はエスケープシーケンスに変換 ( \0, \b, \t, \n, \r, \\ )</span><br>
 *       <div class="blankline">&nbsp;</div>
 *       <b>new SqlString(SqlString.PARAMETER, source)</b> <span class="comment">変換元の文字列が長すぎる場合</span>
 *     </td>
 *   </tr>
 * </table>
 *
 * @since 3.2.0
 * @author Masato Kokubo
 * @see org.lightsleep.helper.TypeConverter
 * @see org.lightsleep.database.Standard
 */
public class MariaDB extends Standard {
    /**
     * パスワードのパターン文字列
     */
    protected static final String PASSWORD_PATTERN = "";

    /**
     * このクラスの唯一のインスタンス
     */
    public static final MariaDB instance = new MariaDB();

    /**
     * <b>MariaDB</b>を構築します。
     */
    protected MariaDB() {
    }

    @Override
    public boolean supportsOffsetLimit() {
        return true;
    }

    @Override
    public String maskPassword(String jdbcUrl) {
        return null;
    }

    @Override
    public Object getObject(Connection connection, ResultSet resultSet, String columnLabel) {
        return null;
    }
}
