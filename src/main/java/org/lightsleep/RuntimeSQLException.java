// RuntimeSQLException.java
// (C) 2016 Masato Kokubo

package org.lightsleep;

/**
 * In this library, Uses RuntimeSQLException instead of SQLException.
 * If a SQLException is thrown while accessing the database, replaces it with this exception and throws.
 * Original SQLException is stored as the cause.
 *
 * @since 1.0
 * @author Masato Kokubo
 */
@SuppressWarnings("serial")
public class RuntimeSQLException extends RuntimeException {
    /**
     * Constructs a new <b>RuntimeSQLException</b> with the specified cause.
     *
     * @param cause the cause (or null if unknown)
     */
    public RuntimeSQLException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new <b>RuntimeSQLException</b> with the specified cause.
     *
     * @param message the detail message
     * @param cause the cause (or null if unknown)
     *
     * @since 2.1.1
     */
    public RuntimeSQLException(String message, Throwable cause) {
        super(message, cause);
    }
}
