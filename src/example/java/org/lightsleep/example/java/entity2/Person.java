// Person.java
// (C) 2016 Masato Kokubo

package org.lightsleep.example.java.entity2;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import org.lightsleep.Sql;
import org.lightsleep.entity.Column;
import org.lightsleep.entity.ColumnProperty;
import org.lightsleep.entity.ColumnType;
import org.lightsleep.entity.Key;
import org.lightsleep.entity.NonInsert;
import org.lightsleep.entity.NonUpdate;
import org.lightsleep.entity.Select;
import org.lightsleep.entity.SelectProperty;
import org.lightsleep.entity.Table;
import org.lightsleep.entity.Update;

// Person
@Table("Contact")
public class Person extends PersonKey {
	public static class Name {
		@Column("firstName")
		public String first;

		@Column("lastName")
		public String last;
	}

	public final Name name = new Name();

	@Column("birthday2")
	@ColumnType(Long.class)
	public Date birthday;

	@NonInsert
	@Update("{updateCount}+1")
	public int updateCount;

	@NonInsert
	@NonUpdate
	public Timestamp createdTime;

	@NonInsert
	@Update("CURRENT_TIMESTAMP")
	public Timestamp updatedTime;

	public Person() {
	}

	public Person(int id) {
		super(id);
	}

	public Person(int id, String firstName, String lastName) {
		super(id);
		name.first = firstName;
		name.last  = lastName;
	}

	public Person(int id, String firstName, String lastName, int year, int month, int day) {
		this(id, firstName, lastName);
		setBirthday(year, month, day);
	}

	public void setBirthday(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(year, month - 1, day, 0, 0, 0);
		birthday = new Date(calendar.getTimeInMillis());
	}

	// Person.Ex
	@Table("super")
	public static class Ex extends Person {
		@Select("{name.first}||' '||{name.last}")
		@NonInsert @NonUpdate
		public String fullName;

		// Person.Ex.DB2
		@Table("super")
		public static class DB2 extends Ex {}

		// Person.Ex.MySQL
		@Table("super")
		@SelectProperty(property="fullName", expression="CONCAT({name.first},' ',{name.last})")
		public static class MySQL extends Ex {}

		// Person.Ex.Oracle
		@Table("super")
		public static class Oracle extends Ex {}

		// Person.Ex.PostgreSQL
		@Table("super")
		public static class PostgreSQL extends Ex {}

		// Person.Ex.SQLite
		@Table("super")
		public static class SQLite extends Ex {}

		// Person.Ex.SQLServer
		@Table("super")
		@SelectProperty(property="fullName", expression="{name.first}+' '+{name.last}")
		public static class SQLServer extends Person {}

		@SuppressWarnings("unchecked")
		public static Class<? extends Ex> targetClass() {
			try {
				return (Class<? extends Ex>)Class.forName(
					Ex.class.getName() + '$' + Sql.getDatabase().getClass().getSimpleName());
			}
			catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}

	// Person.ChildKey
	public static class ChildKey {
		@Key
		@Column("contactId")
		public int personId;

		@Key
		public short childIndex;

		public ChildKey() {
		}

		public ChildKey(int personId, short childIndex) {
			this.personId = personId ;
			this.childIndex = childIndex;
		}
	}

	// Person.Child
	public static abstract class Child extends ChildKey {
		public String label;
		public String content;

		public Child() {
		}

		public Child(int personId, short childIndex, String label, String content) {
			super(personId, childIndex);
			this.label   = label  ;
			this.content = content;
		}
	}

	// Person.Address
	@ColumnProperty(property="content", column="content0")
	public static class Address extends Child {
		public String postCode;
		public String content1;
		public String content2;
		public String content3;

		public Address() {
		}

		public Address(int personId, short childIndex, String label, String postCode, String content, String content1, String content2, String content3) {
			super(personId, childIndex, label, content);
			this.postCode = postCode;
			this.content1 = content1;
			this.content2 = content2;
			this.content3 = content3;
		}
	}

	// Person.Email
	public static class Email extends Child {
		public Email() {
		}

		public Email(int personId, short childIndex, String label, String content) {
			super(personId, childIndex, label, content);
		}
	}

	// Person.Phone
	public static class Phone extends Child {
		public Phone() {
		}

		public Phone(int personId, short childIndex, String label, String content) {
			super(personId, childIndex, label, content);
		}
	}

	// Person.Url
	public static class Url extends Child {
		public Url() {
		}

		public Url(int personId, short childIndex, String label, String content) {
			super(personId, childIndex, label, content);
		}
	}
}