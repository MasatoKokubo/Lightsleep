// ColumnInfo.java
// (C) 2016 Masato Kokubo

package org.lightsleep.helper;

import org.lightsleep.component.Expression;

/**
 * エンティティクラスのプロパティに関連するカラムの情報を持ちます。
 *
 * @since 1.0.0
 * @author Masato Kokubo
 */
public class ColumnInfo {
    /**
     * <b>ColumnInfo</b>を構築します。
     *
     * @param entityInfo エンティティ情報
     * @param propertyName プロパティ名
     * @param columnName カラム名
     * @param columnType カラムの型 (null 可)
     * @param isKey 関連するカラムがキーの一部であれば<b>true</b>、そうでなければ<b>false</b>
     * @param selectExpression SELECT SQLを作成する時に使用する式(式を使用しない場合は、null)
     * @param insertExpression INSERT SQLを作成する時に使用する式(式を使用しない場合は、null)
     * @param updateExpression UPDATE SQLを作成する時に使用する式(式を使用しない場合は、null)
     *     *
     * @throws NullPointerException <b>entityInfo</b>, <b>propertyName</b> または <b>columnName</b>が<b>null</b>の場合
     */
    public ColumnInfo(
        EntityInfo<?> entityInfo, String propertyName, String columnName, Class<?> columnType, boolean isKey,
        Expression selectExpression, Expression insertExpression, Expression updateExpression) {
    }

    /**
     * エンティティ情報を返します。
     *
     * @return エンティティ情報
     */
    public EntityInfo<?> entityInfo() {
        return null;
    }

    /**
     * プロパティ名を返します。
     *
     * @return プロパティ名
     */
    public String propertyName() {
        return null;
    }

    /**
     * 関連するカラム名を返します。
     *
     * @return 関連するカラム名
     */
    public String columnName() {
        return null;
    }

    /**
     * 関連するカラムの型を返します。
     *
     * @return 関連するカラムの型
     *
     * @since 1.8.0
     */
    public Class<?> columnType() {
        return null;
    }

    /**
     * 関連するカラムがキーの一部かどうかを返します。
     *
     * @return 関連するカラムがキーの一部であれば<b>true</b>、そうでなければ<b>false</b>
     */
    public boolean isKey() {
        return false;
    }

    /**
     * 関連するカラムがSELECT SQLで使用されるかどうかを返します。
     *
     * @return 関連するカラムがSELECT SQLで使用される場合は<b>true</b>、そうでなければ<b>false</b>
     */
    public boolean selectable() {
        return false;
    }

    /**
     * 関連するカラムがINSERT SQLで使用されるかどうかを返します。
     *
     * @return 関連するカラムがINSERT SQLで使用される場合は<b>true</b>、そうでなければ<b>false</b>
     */
    public boolean insertable() {
        return false;
    }

    /**
     * 関連するカラムがUPDATE SQLで使用されるかどうかを返します。
     *
     * @return 関連するカラムがUPDATE SQLで使用される場合は<b>true</b>、そうでなければ<b>false</b>
     */
    public boolean updatable() {
        return false;
    }

    /**
     * SELECT SQLを作成する時に使用する式を返します。
     *
     * @return SELECT SQLを作成する時に使用する式(式を使用しない場合は、null)
     */
    public Expression selectExpression() {
        return null;
    }

    /**
     * INSERT SQLを作成する時に使用する式を返します。
     *
     * @return INSERT SQLを作成する時に使用する式(式を使用しない場合は、null)
     */
    public Expression insertExpression() {
        return null;
    }

    /**
     * UPDATE SQLを作成する時に使用する式を返します。
     *
     * @return UPDATE SQLを作成する時に使用する式(式を使用しない場合は、null)
     */
    public Expression updateExpression() {
        return null;
    }

    /**
     * <b>tableAlias</b>が空文字列でなければ、<b>tableAlias + '.' +  <i>プロパティ名</i></b>を返します。
     * 空文字列の場合は、単にプロパティ名を返します。
     *
     * @param tableAlias テーブル別名
     * @return テーブル別名付きのプロパティ名
     *
     * @throws NullPointerException <b>tableAlias</b>が<b>null</b>の場合
     *
     * @since 1.8.2
     */
    public String getPropertyName(String tableAlias) {
        return null;
    }

    /**
     * <b>tableAlias</b>が空文字列でなければ、<b>tableAlias + '.' +  <i>カラム名</i></b>を返します。
     * 空文字列の場合は、単にカラム名を返します。
     *
     * @param tableAlias テーブル別名
     * @return テーブル別名付きのカラム名
     *
     * @throws NullPointerException <b>tableAlias</b>が<b>null</b>の場合
     */
    public String getColumnName(String tableAlias) {
        return null;
    }

    /**
     * <b>tableAlias</b>が空文字列でなければ、<b>tableAlias + '_' + <i>カラム名</i></b>を返します。
     * 空文字列の場合は、単にカラム名を返します。
     *
     * @param tableAlias テーブル別名
     * @return カラム別名
     *
     * @throws NullPointerException <b>tableAlias</b>が<b>null</b>の場合
     */
    public String getColumnAlias(String tableAlias) {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        return false;
    }
}
