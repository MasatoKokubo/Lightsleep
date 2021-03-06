// SqlEntityInfo.java
// (C) 2016 Masato Kokubo

package org.lightsleep.helper;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * An interface with a table alias, an entity information and an entity.
 *
 * @since 1.0
 * @author Masato Kokubo
 */
public interface SqlEntityInfo<E> {
    /**
     * Returns the table reference.
     *
     * @return the table reference
     */
    String tableAlias();

    /**
     * Returns the entity information.
     *
     * @return the entity information
     */
    EntityInfo<E> entityInfo();

    /**
     * Returns the entity that is referenced in expressions.
     *
     * @return the entity
     */
    E entity();

    /**
     * Returns a stream of <b>SqlColumnInfo</b>
     * that is created from all the column information that the entity information own.
     *
     * @return a stream of <b>SqlColumnInfo</b>
     */
    default Stream<SqlColumnInfo> sqlColumnInfoStream() {
        return entityInfo().columnInfos().stream()
                .map(columnInfo -> new SqlColumnInfo(tableAlias(), columnInfo));
    }

    /**
     * Returns a stream of <b>SqlColumnInfo</b>
     * that is created from the column information to match any of specified names.
     *
     * @param names the selected names
     * @return a stream of <b>SqlColumnInfo</b>
     */
    default Stream<SqlColumnInfo> selectedSqlColumnInfoStream(Collection<String> names) {
        return names.isEmpty()
            ? sqlColumnInfoStream()
            : sqlColumnInfoStream()
                .filter(sqlColumnInfo -> names.stream().anyMatch(sqlColumnInfo::matches));
    }
}
