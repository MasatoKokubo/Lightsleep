// Transaction.java
// (C) 2016 Masato Kokubo

package org.lightsleep;

import java.sql.Connection;
import java.sql.SQLException;

import org.lightsleep.connection.ConnectionSupplier;

/**
 * トランザクションを実行するための関数型インターフェースです。
 * 
 * <div class="exampleTitle"><span>使用例 / Java</span></div>
 * <div class="exampleCode"><pre>
 * Transaction.execute(conn -&gt; {
 *     Optional&lt;Contact&gt; contactOpt = new Sql&lt;&gt;(Contact.class)
 *         .where("{id}={}", 1)
 *         .connection(conn)
 *         .select();
 *     contactOpt.ifPresent(contact -&gt; {
 *         contact.setBirthday(2017, 1, 1);
 *         new Sql&lt;&gt;(Contact.class)
 *             .connection(conn)
 *             .update(contact);
 *     });
 * });
 * </pre></div>
 *
 * <div class="exampleTitle"><span>使用例 / Groovy</span></div>
 * <div class="exampleCode"><pre>
 * Transaction.execute {
 *     def contactOpt = new Sql&lt;&gt;(Contact)
 *         .where('{id}={}', 1)
 *         .connection(it)
 *         .select()
 *     contactOpt.ifPresent {Contact contact -&gt;
 *         contact.setBirthday(2017, 1, 1)
 *         new Sql&lt;&gt;(Contact)
 *             .connection(it)
 *             .update(contact)
 *     }
 * }
 * </pre></div>
 *
 * @since 1.0
 * @author Masato Kokubo
 */
@FunctionalInterface
public interface Transaction {
	/**
	 * トランザクションの本体をこのメソッドに記述します。
	 *
	 * @param connection データベース・コネクション
	 *
	 * @throws RuntimeSQLException データベースのアクセス中に <b>SQLException</b> がスローされた場合
	 */
	void executeBody(Connection connection);

	/**
	 * 以下の順でトランザクションを実行します。
	 *
	 * <ol>
	 *   <li><b>Sql.connectionSupplier().get()</b> をコールしてデータベース・コネクションを取得</li>
	 *   <li><b>transaction.executeBody</b> メソッドを実行</li>
	 *   <li>トランザクションをコミット</li>
	 *   <li>データベース・コネクションをクローズ</li>
	 * </ol>
	 *
	 * <p>
	 * トランザクションの本体の実行中に例外がスローされた場合、コミットではなくロールバックを行います。
	 * </p>
	 *
	 * <p>
	 * <b>transaction</b> にラムダ式でトランザクションの実体を記述してください。
	 * </p>
	 *
	 * @param transaction <b>Transaction</b> オブジェクト
	 *
	 * @throws NullPointerException <b>transaction</b> が null の場合
	 * @throws RuntimeSQLException データベースのアクセス中に <b>SQLException</b> がスローされた場合
	 */
	static void execute(Transaction transaction) {
	}

	/**
	 * 以下の順でトランザクションを実行します。
	 *
	 * <ol>
	 *   <li><b>connectionSupplier.get()</b> をコールしてデータベース・コネクションを取得</li>
	 *   <li><b>transaction.executeBody</b> メソッドを実行</li>
	 *   <li>トランザクションをコミット</li>
	 *   <li>データベース・コネクションをクローズ</li>
	 * </ol>
	 *
	 * <p>
	 * トランザクションの本体の実行中に例外がスローされた場合、コミットではなくロールバックを行います。
	 * </p>
	 *
	 * <p>
	 * <b>transaction</b> にラムダ式でトランザクションの実体を記述してください。
	 * </p>
	 *
	 * @param connectionSupplier <b>ConnectionSupplier</b> オブジェクト
	 * @param transaction <b>Transaction</b> オブジェクト
	 *
	 * @throws NullPointerException <b>connectionSupplier</b> または <b>transaction</b> が null の場合
	 * @throws RuntimeSQLException データベースのアクセス中に <b>SQLException</b> がスローされた場合
	 *
	 * @since 1.5.0
	 */
	static void execute(ConnectionSupplier connectionSupplier, Transaction transaction) {
	}

	/**
	 * コネクションが自動コミットでなければ、トランザクションをコミットします。
	 *
	 * @param connection データベース・コネクション
	 *
	 * @throws RuntimeSQLException データベース・アクセス・エラーが発生した場合
	 */
	static void commit(Connection connection) {
	}

	/**
	 * コネクションが自動コミットでなければ、トランザクションをロールバックします。
	 *
	 * @param connection データベース・コネクション
	 *
	 * @throws RuntimeSQLException データベースのアクセス中に <b>SQLException</b> がスローされた場合
	 */
	static void rollback(Connection connection) {
	}
}
