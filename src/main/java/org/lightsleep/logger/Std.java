// Std.java
// (C) 2016 Masato Kokubo

package org.lightsleep.logger;

import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.function.Supplier;
import java.util.Objects;

/**
 * Outputs logs to stdout or stderr.
 *
 * @since 1.0.0
 * @author Masato Kokubo
 */
public abstract class Std implements Logger {
    /**
     * Outputs logs to stdout.
     */
    public static abstract class Out extends Std {
        /**
         * Constructs a new <b>Std.Out</b> with the logging level.
         *
         * @param level the logging level
         */
        public Out(Level level) {
            super(System.out, level);
        }

        /**
         * Outputs logs to stdout in the trace logging level.
         */
        public static class Trace extends Out {
            /**
             * Constructs a new <b>Std.Out.Trace</b>.
             *
             * @param name the name (does not use)
             */
            public Trace(String name) {super(Level.TRACE);}
        }

        /**
         * Outputs logs to stdout in the debug logging level.
         */
        public static class Debug extends Out {
            /**
             * Constructs a new <b>Std.Out.Debug</b>.
             *
             * @param name the name (does not use)
             */
            public Debug(String name) {super(Level.DEBUG);}
        }

        /**
         * Outputs logs to stdout in the info logging level.
         */
        public static class Info extends Out {
            /**
             * Constructs a new <b>Std.Out.Info</b>.
             *
             * @param name the name (does not use)
             */
            public Info (String name) {super(Level.INFO );}
        }

        /**
         * Outputs logs to stdout in the warn logging level.
         */
        public static class Warn extends Out {
            /**
             * Constructs a new <b>Std.Out.Warn</b>.
             *
             * @param name the name (does not use)
             */
            public Warn (String name) {super(Level.WARN );}
        }

        /**
         * Outputs logs to stdout in the error logging level.
         */
        public static class Error extends Out {
            /**
             * Constructs a new <b>Std.Out.Error</b>.
             *
             * @param name the name (does not use)
             */
            public Error(String name) {super(Level.ERROR);}
        }

        /**
         * Outputs logs to stdout in the fatal logging level.
         */
        public static class Fatal extends Out {
            /**
             * Constructs a new <b>Std.Out.Fatal</b>.
             *
             * @param name the name (does not use)
             */
            public Fatal(String name) {super(Level.FATAL);}
        }
    }

    /**
     * Outputs logs to stderr.
     */
    public static abstract class Err extends Std {
        /**
         * Constructs a new <b>Std.Err</b> with the logging level.
         *
         * @param level the logging level
         */
        public Err(Level level) {
            super(System.err, level);
        }

        /**
         * Outputs logs to stderr in the trace logging level.
         */
        public static class Trace extends Err {
            /**
             * Constructs a new <b>Std.Err.Trace</b>.
             *
             * @param name the name (does not use)
             */
            public Trace(String name) {super(Level.TRACE);}
        }

        /**
         * Outputs logs to stderr in the debug logging level.
         */
        public static class Debug extends Err {
            /**
             * Constructs a new <b>Std.Err.Debug</b>.
             *
             * @param name the name (does not use)
             */
            public Debug(String name) {super(Level.DEBUG);}
        }

        /**
         * Outputs logs to stderr in the info logging level.
         */
        public static class Info extends Err {
            /**
             * Constructs a new <b>Std.Err.Info</b>.
             *
             * @param name the name (does not use)
             */
            public Info (String name) {super(Level.INFO );}
        }

        /**
         * Outputs logs to stderr in the warn logging level.
         */
        public static class Warn extends Err {
            /**
             * Constructs a new <b>Std.Err.Warn</b>.
             *
             * @param name the name (does not use)
             */
            public Warn (String name) {super(Level.WARN );}
        }

        /**
         * Outputs logs to stderr in the error logging level.
         */
        public static class Error extends Err {
            /**
             * Constructs a new <b>Std.Err.Error</b>.
             *
             * @param name the name (does not use)
             */
            public Error(String name) {super(Level.ERROR);}
        }

        /**
         * Outputs logs to stderr in the fatal logging level.
         */
        public static class Fatal extends Err {
            /**
             * Constructs a new <b>Std.Err.Fatal</b>.
             *
             * @param name the name (does not use)
             */
            public Fatal(String name) {super(Level.FATAL);}
        }
    }

    /** The logger level */
    protected enum Level {TRACE, DEBUG, INFO, WARN, ERROR, FATAL}

    // The print stream
    private PrintStream stream;

    // The level
    private Level level;

    // The message format
    private static String messageFormat = "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %2$s ";

    // StackTraceElement format
    // %1: class name
    // %2: mwthod name
    // %3: file name
    // %4: line number
    private static String stackTraceFormat = "    at %1$s.%2$s (%3$s:%4$d)";

    /**
     * Constructs a new <b>Std</b>.
     *
     * @param stream the print stream
     * @param level the logging level
     */
    protected Std(PrintStream stream, Level level) {
        this.stream = Objects.requireNonNull(stream, "stream is null");
        this.level  = Objects.requireNonNull(level , "level is null");
    }

    /**
     * Outputs a message to the log at <b>level</b>.
     *
     * @param level the level
     * @param message a message
     */
    private void println(Level level, String message) {
        println(level, message, null);
    }

    /**
     * Outputs a message with a <b>Throwable</b> to the log at <b>level</b>.
     *
     * @param level the level
     * @param message a message
     * @param t a Throwable (nullable)
     */
    private void println(Level level, String message, Throwable t) {
        if (level.compareTo(this.level) < 0)
            return;

        stream.println(String.format(messageFormat, new Timestamp(System.currentTimeMillis()), level) + message);

        boolean isCause = false;
        int elementsCount = 0;
        while (t != null) {
            stream.println(isCause ? "Caused by: " + t.toString() : t.toString());
            StackTraceElement[] elements = t.getStackTrace();
            if (!isCause)
                elementsCount = elements.length;
            for (int index = 0; index < elements.length; ++index) {
                StackTraceElement element = elements[index];
                if (isCause && index > elements.length - elementsCount) {
                    stream.println("    ..." + (elementsCount - 1) + " more");
                    break;
                }
                stream.println(String.format(stackTraceFormat,
                    element.getClassName(), element.getMethodName(),
                    element.getFileName(), element.getLineNumber()));
            }
            t = t.getCause();
            isCause = true;
        }
    ////
    }

    @Override
    public void trace(String message) {
        println(Level.TRACE, message);
    }

    @Override
    public void debug(String message) {
        println(Level.DEBUG, message);
    }

    @Override
    public void info(String message) {
        println(Level.INFO, message);
    }

    @Override
    public void warn(String message) {
        println(Level.WARN, message);
    }

    @Override
    public void error(String message) {
        println(Level.ERROR, message);
    }

    @Override
    public void fatal(String message) {
        println(Level.FATAL, message);
    }

    @Override
    public void trace(String message, Throwable t) {
        println(Level.TRACE, message, t);
    }

    @Override
    public void debug(String message, Throwable t) {
        println(Level.DEBUG, message, t);
    }

    @Override
    public void info(String message, Throwable t) {
        println(Level.INFO,message, t);
    }

    @Override
    public void warn(String message, Throwable t) {
        println(Level.WARN, message, t);
    }

    @Override
    public void error(String message, Throwable t) {
        println(Level.ERROR, message, t);
    }

    @Override
    public void fatal(String message, Throwable t) {
        println(Level.FATAL, message, t);
    }

    @Override
    public void trace(Supplier<String> messageSupplier) {
        trace(messageSupplier.get());
    }

    @Override
    public void debug(Supplier<String> messageSupplier) {
        debug(messageSupplier.get());
    }

    @Override
    public void info(Supplier<String> messageSupplier) {
        info(messageSupplier.get());
    }

    @Override
    public void warn(Supplier<String> messageSupplier) {
        warn(messageSupplier.get());
    }

    @Override
    public void error(Supplier<String> messageSupplier) {
        error(messageSupplier.get());
    }

    @Override
    public void fatal(Supplier<String> messageSupplier) {
        fatal(messageSupplier.get());
    }

    @Override
    public boolean isTraceEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isFatalEnabled() {
        return true;
    }
}
