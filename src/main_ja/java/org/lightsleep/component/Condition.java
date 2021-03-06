// Condition.java
// (C) 2016 Masato Kokubo

package org.lightsleep.component;

import java.util.Collection;
import java.util.stream.Stream;

import org.lightsleep.Sql;

/**
 * 条件インタフェースです。
 *
 * @since 1.0.0
 * @author Masato Kokubo
 */
public interface Condition extends SqlComponent {
    /** 空の条件 */
    static final Condition EMPTY = null;

    /** 全行を対象する条件 */
    static final Condition ALL = null;

    /**
     * 条件式を生成して返します。
     *
     * @param content 条件式の文字列内容
     * @param arguments 条件式に埋め込む引数配列
     * @return 条件式
     *
     * @throws NullPointerException <b>content</b>または<b>arguments</b>が<b>null</b>の場合
     *
     * @see Expression#Expression(String, Object...)
     */
    static Condition of(String content, Object... arguments) {
        return null;
    }

    /**
     * エンティティ条件を生成して返します。
     *
     * @param <E> エンティティの型
     * @param entity エンティティ
     * @return エンティティ条件
     *
     * @see EntityCondition#EntityCondition(Object)
     */
    static <E> Condition of(E entity) {
        return null;
    }

    /**
     * サブクエリ条件を生成して返します。
     *
     * @param <E> 外側のクエリの対象テーブルに対応するエンティティの型
     * @param <SE> サブクエリの対象テーブルに対応するエンティティの型
     * @param content サブクエリのSELECT 文より左部分の式の文字列内容
     * @param outerSql 構文上<b>subSql</b>の外側にある<b>Sql</b>オブジェクト
     * @param subSql サブクエリ生成用のSqlオブジェクト
     * @return サブクエリ条件
     *
     * @throws NullPointerException <b>content</b>, <b>outerSql</b>または<b>subSql</b>が<b>null</b>の場合
     *
     * @see #of(Sql, Sql, String)
     * @see SubqueryCondition#SubqueryCondition(Expression, Sql, Sql)
     */
    static <E, SE> Condition of(String content, Sql<E> outerSql, Sql<SE> subSql) {
        return null;
    }

    /**
     * サブクエリ条件を生成して返します。
     *
     * @param <E> 外側のクエリの対象テーブルに対応するエンティティの型
     * @param <SE> サブクエリの対象テーブルに対応するエンティティの型
     * @param outerSql 構文上<b>subSql</b>の外側にある<b>Sql</b>オブジェクト
     * @param content サブクエリのSELECT 文より左部分の式の文字列内容
     * @param subSql サブクエリ生成用のSqlオブジェクト
     * @return サブクエリ条件
     *
     * @throws NullPointerException <b>outerSql</b>, <b>subSql</b>または<b>content</b>が<b>null</b>の場合
     *
     * @see #of(String, Sql, Sql)
     * @see SubqueryCondition#SubqueryCondition(Expression, Sql, Sql)
     */
    static <E, SE> Condition of(Sql<E> outerSql, Sql<SE> subSql, String content) {
        return null;
    }

    /**
     * 否定条件を返します。
     *
     * @return NOT(この条件)
     *
     * @see Not#Not(Condition)
     * @see LogicalCondition#optimized()
     */
    default Condition not() {
        return null;
    }

    /**
     * (この条件 AND 指定の条件)を返します。
     *
     * @param condition 条件
     * @return この条件 AND 指定の条件
     *
     * @throws NullPointerException <b>condition</b>が<b>null</b>の場合
     *
     * @see And#And(Condition...)
     * @see LogicalCondition#optimized()
     */
    default Condition and(Condition condition) {
        return null;
    }

    /**
     * (この条件 AND 指定の条件)を返します。
     *
     * @param content 条件式の文字列内容
     * @param arguments 条件式に埋め込む引数配列
     *
     * @return この条件 AND 指定の条件
     *
     * @throws NullPointerException <b>content</b>または<b>arguments</b>が<b>null</b>の場合
     *
     * @see And#And(Condition...)
     * @see Expression#Expression(String, Object...)
     * @see LogicalCondition#optimized()
     */
    default Condition and(String content, Object... arguments) {
        return null;
    }

    /**
     * (この条件 AND 指定のエンティティ条件)を返します。
     *
     * @param <K> エンティティの型
     * @param entity エンティティ条件のエンティティ
     *
     * @return この条件 AND 指定の条件
     *
     * @throws NullPointerException <b>entity</b>が<b>null</b>の場合
     *
     * @since 3.1.0
     * @see And#And(Condition...)
     * @see EntityCondition#EntityCondition(Object)
     */
    default <K> Condition and(K entity) {
        return null;
    }

    /**
     * (この条件 AND 指定の条件)を返します。
     *
     * @param <E> 外側のクエリの対象テーブルに対応するエンティティの型
     * @param <SE> サブクエリの対象テーブルに対応するエンティティの型
     * @param content サブクエリのSELECT 文より左部分の式の文字列内容
     * @param outerSql 構文上<b>subSql</b>の外側にある<b>Sql</b>オブジェクト
     * @param subSql サブクエリ用のSqlオブジェクト
     * @return この条件 AND 指定の条件
     *
     * @throws NullPointerException <b>content</b>, <b>outerSql</b>または<b>subSql</b>が<b>null</b>の場合
     *
     * @see #and(Sql, Sql, String)
     * @see And#And(Condition...)
     * @see SubqueryCondition#SubqueryCondition(Expression, Sql, Sql)
     * @see Expression#Expression(String, Object...)
     * @see LogicalCondition#optimized()
     */
    default <E, SE> Condition and(String content, Sql<E> outerSql, Sql<SE> subSql) {
        return null;
    }

    /**
     * (この条件 AND 指定の条件)を返します。
     *
     * @param <E> 外側のクエリの対象テーブルに対応するエンティティの型
     * @param <SE> サブクエリの対象テーブルに対応するエンティティの型
     * @param outerSql 構文上<b>subSql</b>の外側にある<b>Sql</b>オブジェクト
     * @param subSql サブクエリ用のSqlオブジェクト
     * @param content サブクエリのSELECT 文より右部分の式の文字列内容
     * @return この条件 AND 指定の条件
     *
     * @throws NullPointerException <b>content</b>, <b>outerSql</b>または<b>subSql</b>が<b>null</b>の場合
     *
     * @since 3.1.0
     * @see #and(String, Sql, Sql)
     * @see And#And(Condition...)
     * @see SubqueryCondition#SubqueryCondition(Expression, Sql, Sql)
     * @see Expression#Expression(String, Object...)
     * @see LogicalCondition#optimized()
     */
    default <E, SE> Condition and(Sql<E> outerSql, Sql<SE> subSql, String content) {
        return null;
    }

    /**
     * (この条件 OR 指定の条件)を返します。
     *
     * @param condition 条件
     * @return この条件 OR 指定の条件
     *
     * @throws NullPointerException <b>condition</b>が<b>null</b>の場合
     *
     * @see Or#Or(Condition...)
     * @see LogicalCondition#optimized()
     */
    default Condition or(Condition condition) {
        return null;
    }

    /**
     * (この条件 OR 指定の条件)を返します。
     *
     * @param content 条件式の文字列内容
     * @param arguments 条件式に埋め込む引数配列
     * @return この条件 OR 指定の条件
     *
     * @throws NullPointerException <b>content</b>または<b>arguments</b>が<b>null</b>の場合
     *
     * @see Or#Or(Condition...)
     * @see Expression#Expression(String, Object...)
     * @see LogicalCondition#optimized()
     */
    default Condition or(String content, Object... arguments) {
        return null;
    }

    /**
     * (この条件 OR 指定のエンティティ条件)を返します。
     *
     * @param <K> エンティティの型
     * @param entity エンティティ条件のエンティティ
     *
     * @return この条件 AND 指定の条件
     *
     * @throws NullPointerException <b>entity</b>が<b>null</b>の場合
     *
     * @since 3.1.0
     * @see Or#Or(Condition...)
     * @see EntityCondition#EntityCondition(Object)
     */
    default <K> Condition or(K entity) {
        return null;
    }

    /**
     * (この条件 OR 指定の条件)を返します。
     *
     * @param <E> 外側のクエリの対象テーブルに対応するエンティティの型
     * @param <SE> サブクエリの対象テーブルに対応するエンティティの型
     * @param content サブクエリのSELECT 文より左部分の式の文字列内容
     * @param outerSql 構文上<b>subSql</b>の外側にある<b>Sql</b>オブジェクト
     * @param subSql サブクエリ用のSqlオブジェクト
     * @return この条件 OR 指定の条件
     *
     * @throws NullPointerException <b>content</b>, <b>outerSql</b>または<b>subSql</b>が<b>null</b>の場合
     *
     * @see #or(Sql, Sql, String)
     * @see Or#Or(Condition...)
     * @see SubqueryCondition#SubqueryCondition(Expression, Sql, Sql)
     * @see Expression#Expression(String, Object...)
     * @see LogicalCondition#optimized()
     */
    default <E, SE> Condition or(String content, Sql<E> outerSql, Sql<SE> subSql) {
        return null;
    }

    /**
     * (この条件 OR 指定の条件)を返します。
     *
     * @param <E> 外側のクエリの対象テーブルに対応するエンティティの型
     * @param <SE> サブクエリの対象テーブルに対応するエンティティの型
     * @param outerSql 構文上<b>subSql</b>の外側にある<b>Sql</b>オブジェクト
     * @param subSql サブクエリ用のSqlオブジェクト
     * @param content サブクエリのSELECT 文より右部分の式の文字列内容
     * @return この条件 OR 指定の条件
     *
     * @throws NullPointerException <b>outerSql</b>, <b>subSql</b>または<b>content</b>が<b>null</b>の場合
     *
     * @since 3.1.0
     * @see #or(String, Sql, Sql)
     * @see Or#Or(Condition...)
     * @see SubqueryCondition#SubqueryCondition(Expression, Sql, Sql)
     * @see Expression#Expression(String, Object...)
     * @see LogicalCondition#optimized()
     */
    default <E, SE> Condition or(Sql<E> outerSql, Sql<SE> subSql, String content) {
        return null;
    }

    /**
     * <b>new And(conditions)</b>を最適化した条件を返します。
     *
     * @param conditions 条件のストリーム
     * @return <b>new And(conditions)</b>を最適化した条件
     *
     * @throws NullPointerException <b>conditions</b>か<b>conditions</b>の要素のいずれかが<b>null</b>の場合
     *
     * @since 1.8.8
     *
     * @see And#And(Stream)
     * @see LogicalCondition#optimized()
     */
    static Condition and(Stream<Condition> conditions) {
        return null;
    }

    /**
     * <b>new And(conditions)</b>を最適化した条件を返します。
     *
     * @param conditions 条件のコレクション
     * @return <b>new And(conditions)</b>を最適化した条件
     *
     * @throws NullPointerException <b>conditions</b>か<b>conditions</b>の要素のいずれかが<b>null</b>の場合
     *
     * @since 1.8.8
     *
     * @see And#And(Collection)
     * @see LogicalCondition#optimized()
     */
    static Condition and(Collection<Condition> conditions) {
        return null;
    }

    /**
     * <b>new And(conditions)</b>を最適化した条件を返します。
     *
     * @param conditions 条件の配列
     * @return <b>new And(conditions)</b>を最適化した条件
     *
     * @throws NullPointerException <b>conditions</b>か<b>conditions</b>の要素のいずれかが<b>null</b>の場合
     *
     * @since 1.8.8
     *
     * @see And#And(Condition...)
     * @see LogicalCondition#optimized()
     */
    static Condition and(Condition... conditions) {
        return null;
    }

    /**
     * <b>new Or(conditions)</b>を最適化した条件を返します。
     *
     * @param conditions 条件のストリーム
     * @return <b>new Or(conditions)</b>を最適化した条件
     *
     * @throws NullPointerException <b>conditions</b>か<b>conditions</b>の要素のいずれかが<b>null</b>の場合
     *
     * @since 1.8.8
     *
     * @see Or#Or(Stream)
     * @see LogicalCondition#optimized()
     */
    static Condition or(Stream<Condition> conditions) {
        return null;
    }

    /**
     * <b>new Or(conditions)</b>を最適化した条件を返します。
     *
     * @param conditions 条件のコレクション
     * @return <b>new Or(conditions)</b>を最適化した条件
     *
     * @throws NullPointerException <b>conditions</b>か<b>conditions</b>の要素のいずれかが<b>null</b>の場合
     *
     * @since 1.8.8
     *
     * @see Or#Or(Collection)
     * @see LogicalCondition#optimized()
     */
    static Condition or(Collection<Condition> conditions) {
        return null;
    }

    /**
     * <b>new Or(conditions)</b>を最適化した条件を返します。
     *
     * @param conditions 条件の配列
     * @return <b>new Or(conditions)</b>を最適化した条件
     *
     * @throws NullPointerException <b>conditions</b>か<b>conditions</b>の要素のいずれかが<b>null</b>の場合
     *
     * @since 1.8.8
     *
     * @see Or#Or(Condition...)
     * @see LogicalCondition#optimized()
     */
    static Condition or(Condition... conditions) {
        return null;
    }
}
