// EntityCondition.java
// (C) 2016 Masato Kokubo

package org.lightsleep.component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

import org.lightsleep.Sql;
import org.lightsleep.database.Database;
import org.lightsleep.helper.Accessor;
import org.lightsleep.helper.EntityInfo;
import org.lightsleep.helper.Resource;

/**
 * Configure a condition using the value of the primary key of an entity.
 *
 * @param <K> the type of the entity
 *
 * @since 1.0.0
 * @author Masato Kokubo
 */
public class EntityCondition<K> implements Condition {
    // Class resources
    private static final Resource resource = new Resource(EntityCondition.class);
    private static final String messageEntityNotHaveKeyColumns = resource.getString("messageEntityNotHaveKeyColumns");

    // The entity
    private K entity;

    // The entity info
    private EntityInfo<K> entityInfo;

    /**
     * Constructs a new <b>EntityCondition</b>.
     *
     * @param entity the entity
     *
     * @throws NullPointerException <b>entity</b> is <b>null</b>
     */
    @SuppressWarnings("unchecked")
    public EntityCondition(K entity) {
        this.entity = Objects.requireNonNull(entity, "entity is null");

        entityInfo = Sql.getEntityInfo((Class<K>)entity.getClass());
        if (entityInfo.keyColumnInfos().size() == 0)
            throw new IllegalArgumentException(MessageFormat.format(messageEntityNotHaveKeyColumns, entityInfo.entityClass()));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public <E> String toString(Database database, Sql<E> sql, List<Object> parameters) {
        String tableAlias = Objects.requireNonNull(sql, "sql is null").tableAlias();
        Accessor<K> accessor = entityInfo.accessor();

        Condition[] condition = new Condition[] {Condition.EMPTY};

        entityInfo.keyColumnInfos()
            .forEach(columnInfo -> {
                String propertyName = columnInfo.propertyName();
                String columnName = columnInfo.getColumnName(tableAlias);
                condition[0] = condition[0].and(columnName + "={}", accessor.getValue(entity, propertyName));
            });

        return condition[0].toString(database, sql, parameters);
    }
}
