// Numbering.java
// (C) 2016 Masato Kokubo

package org.lightsleep.test.entity;

import java.sql.Connection;
import java.util.Optional;

import org.lightsleep.Sql;
import org.lightsleep.Transaction;
import org.lightsleep.database.SQLite;
import org.lightsleep.entity.Insert;
import org.lightsleep.entity.Key;
import org.lightsleep.entity.Update;
import org.lightsleep.helper.EntityInfo;

/**
 * The entity of numbering table.
 * Numbering Entity.
 *
 * @since 1.0
 * @author Masato Kokubo
 */
public class Numbering {
	/**
	 * Gets and returns a new Identifier of the specified enyity class.
	 *
	 * @param <E> the entity type
	 * @param entityClass the entity class
	 * @return a new Identifier
	 */
	public static synchronized <E extends Common> int getNewId(Connection connection, Class<E> entityClass) {
		EntityInfo<E> entityInfo = Sql.getEntityInfo(entityClass);
		String tableName = entityInfo.tableName();

		Optional<Numbering> numberingOpt = new Sql<>(Numbering.class)
			.where("{tableName}={}", tableName)
			.doIf(!(Sql.getDatabase() instanceof SQLite), Sql::forUpdate)
			.select(connection);

		int id[] = new int[1];
		if (numberingOpt.isPresent()) {
			Numbering numbering = numberingOpt.get();
			new Sql<>(Numbering.class).update(connection, numbering);
			id[0] = numbering.nextId;
		} else {
			Numbering numbering = new Numbering(tableName);
			new Sql<>(Numbering.class).insert(connection, numbering);
			id[0] = 1;
		}

		Transaction.commit(connection);

		return id[0];
	}

	/** Table Name */
	@Key
	public String tableName;

	/** Update Count */
	@Insert("2")
	@Update("{nextId}+1")
	public int nextId;

	/**
	 * Constructs a Numbering.
	 */
	public Numbering() {
	}

	/**
	 * Constructs a Numbering.
	 *
	 *	@param tableName the table name
	 */
	public Numbering(String tableName) {
		this.tableName = tableName;
	}
}
