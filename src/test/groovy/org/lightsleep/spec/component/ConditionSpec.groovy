// ConditionSpec.groovy
// (C) 2016 Masato Kokubo

package org.lightsleep.spec.component

import org.debugtrace.DebugTrace
import org.lightsleep.*
import org.lightsleep.component.*
import org.lightsleep.entity.*
import spock.lang.*

// ConditionSpec
@Unroll
class ConditionSpec extends Specification {
	static class Entity {
		@Key
		int id
	}

	def "Condition.isEmpty"() {
		expect:
			Condition.EMPTY.empty
	}

	def "Condition.of"() {
		expect:
			Condition.of('').empty
			Condition.of('A') instanceof Expression
			Condition.of('A', 1) instanceof Expression
			Condition.of('{}', 'A') instanceof Expression
			Condition.of(new Entity()) instanceof EntityCondition
			Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class)) instanceof SubqueryCondition

	}

	def "Condition.not"() {
		setup:
			def sql = new Sql<>(Entity.class)

		expect:
			Condition.EMPTY.not() == Condition.EMPTY
			Condition.of('A').not() instanceof Not
			Condition.of('A', 1).not() instanceof Not
			Condition.of('{}', 'A').not() instanceof Not
			Condition.of(new Entity()).not() instanceof Not
			Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class)).not() instanceof Not

			Condition.EMPTY.not().not() == Condition.EMPTY
			Condition.of('A').not().not() instanceof Expression
			Condition.of('A', 1).not().not() instanceof Expression
			Condition.of('{}', 'A').not().not() instanceof Expression
			Condition.of(new Entity()).not().not() instanceof EntityCondition
			Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class)).not().not() instanceof SubqueryCondition

		when:
			def condition1 = Condition.of('A')
			def condition2 = condition1.not()

		then:
			condition1.toString(sql, []) == 'A'
			condition2.toString(sql, []) == 'NOT(A)'
	}

	def "Condition.and"() {
		setup:
			def sql = new Sql<>(Entity.class)

		expect:
			Condition.of('A').and(Condition.of('A')) instanceof And
			Condition.of('A', 1).and(Condition.of('A', 1)) instanceof And
			Condition.of('{}', 'A').and(Condition.of('{}', 'A')) instanceof And
			Condition.of(new Entity()).and(Condition.of(new Entity())) instanceof And
			Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class))
				.and(Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class))) instanceof And

			Condition.EMPTY.and(Condition.EMPTY) == Condition.EMPTY
			Condition.EMPTY.and(Condition.of('A')) instanceof Expression
			Condition.EMPTY.and(Condition.of('A', 1)) instanceof Expression
			Condition.EMPTY.and(Condition.of('{}', 'A')) instanceof Expression
			Condition.EMPTY.and(Condition.of(new Entity())) instanceof EntityCondition
			Condition.EMPTY.and(Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class))) instanceof SubqueryCondition

			Condition.of('A').and(Condition.EMPTY) instanceof Expression
			Condition.of('A', 1).and(Condition.EMPTY) instanceof Expression
			Condition.of('{}', 'A').and(Condition.EMPTY) instanceof Expression
			Condition.of(new Entity()).and(Condition.EMPTY) instanceof EntityCondition
			Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class)).and(Condition.EMPTY) instanceof SubqueryCondition

		when:
			def condition1 = Condition.of('A')
			def condition2 = condition1.and('B')
			def condition3 = Condition.of('C')
			def condition4 = condition3.and('D')
			def condition5 = condition2.or(condition4)

		then:
			condition1.toString(sql, []) == 'A'
			condition2.toString(sql, []) == 'A AND B'
			condition3.toString(sql, []) == 'C'
			condition4.toString(sql, []) == 'C AND D'
			condition5.toString(sql, []) == 'A AND B OR C AND D'
	}

	def "Condition.or"() {
		setup:
			def sql = new Sql<>(Entity.class)

		expect:
			Condition.of('A').or(Condition.of('A')) instanceof Or
			Condition.of('A', 1).or(Condition.of('A', 1)) instanceof Or
			Condition.of('{}', 'A').or(Condition.of('{}', 'A')) instanceof Or
			Condition.of(new Entity()).or(Condition.of(new Entity())) instanceof Or
			Condition.of('', new Sql<>(Entity.class), new Sql<>(Entity.class))
				.or(Condition.of('', new Sql<>(Entity.class), new Sql<>(Entity.class))) instanceof Or

			Condition.EMPTY.or(Condition.EMPTY) == Condition.EMPTY
			Condition.EMPTY.or(Condition.of('A')) instanceof Expression
			Condition.EMPTY.or(Condition.of('A', 1)) instanceof Expression
			Condition.EMPTY.or(Condition.of('{}', 'A')) instanceof Expression
			Condition.EMPTY.or(Condition.of(new Entity())) instanceof EntityCondition
			Condition.EMPTY.or(Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class))) instanceof SubqueryCondition

			Condition.of('A').or(Condition.EMPTY) instanceof Expression
			Condition.of('A', 1).or(Condition.EMPTY) instanceof Expression
			Condition.of('{}', 'A').or(Condition.EMPTY) instanceof Expression
			Condition.of(new Entity()).or(Condition.EMPTY) instanceof EntityCondition
			Condition.of('A', new Sql<>(Entity.class), new Sql<>(Entity.class)).or(Condition.EMPTY) instanceof SubqueryCondition

		when:
			def condition1 = Condition.of('A')
			def condition2 = condition1.or('B')
			def condition3 = Condition.of('C')
			def condition4 = condition3.or('D')
			def condition5 = condition2.and(condition4)

		then:
			condition1.toString(sql, []) == 'A'
			condition2.toString(sql, []) == 'A OR B'
			condition3.toString(sql, []) == 'C'
			condition4.toString(sql, []) == 'C OR D'
			condition5.toString(sql, []) == '(A OR B) AND (C OR D)'
	}
}