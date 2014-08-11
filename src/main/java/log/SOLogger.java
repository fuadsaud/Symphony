package log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Simple logging service. You can set up handlers for logger output. Logs
 * include time, class name, method name and line number (where logger is
 * called), a "level" (three letters code), and a message.
 * 
 * @author Fuad Saud
 * 
 */
public class SOLogger {

	public enum Level {

		DEBUG("DBG"), INFO("INF"), ERROR("ERR"), EXCEPTION("EXC"), WARNING("WAR");

		private final String name;

		private Level(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	/**
	 * Indicates whether the logs should be send to the console.
	 */
	private static boolean loggingActivated = true;

	private static final ArrayList<PrintStream> handlers = new ArrayList<PrintStream>();

	static {
		handlers.add(System.err);
	}

	public static void addLogHandler(PrintStream out) {
		if (out != null) {
			handlers.add(out);
		}
	}

	public static void debug(Object log) {
		log(Level.DEBUG, log.toString());
	}

	public static void error(Object log) {
		log(Level.ERROR, log.toString());
	}

	public static void exception(Object log) {
		log(Level.EXCEPTION, log.toString());
	}

	public static void info(Object log) {
		log(Level.INFO, log.toString());
	}

	/**
	 * Checks if logs are being sent to the console.
	 * 
	 * @return <code>true</code> in case messages are being printed to the
	 *         console
	 */
	public static boolean isLoggingActivated() {
		return loggingActivated;
	}

	public static void log(Level level, Object log) {
		if (!isLoggingActivated()) {
			return;
		}

		StackTraceElement ste = currentStackTraceElement();

		String fileName = simpleClassName(ste.getClassName());
		String methodName = ste.getMethodName();
		String time = timeString();
		int lineNumber = ste.getLineNumber();

		for (PrintStream out : handlers) {
			out.print(time + " [" + level.getName() + "] (" + fileName + "." + methodName + ":"
					+ lineNumber + ")");

			out.println(" " + log);

			out.flush();
		}
	}

	public static void log(Object log) {
		log(Level.INFO, log.toString());
	}

	public static void setLoggingActivated(boolean loggingActivated) {
		SOLogger.loggingActivated = loggingActivated;
	}

	public static void warning(Object log) {
		log(Level.WARNING, log.toString());
	}

	/**
	 * Finds the {@link StackTraceElement} representing the call to the Logger
	 * (may present bugs if the call is made inside {@link SOLogger} class.
	 * 
	 * @return the {@link StackTraceElement} representing the call to the logger
	 */
	private static StackTraceElement currentStackTraceElement() {
		StackTraceElement ret = null;
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		for (StackTraceElement ste : stes) {

			if (!simpleClassName(ste.getClassName()).equals("SOLogger")
					&& !simpleClassName(ste.getClassName()).equals("Thread")) {
				return ste;
			} else {
				ret = ste;
			}
		}
		return ret;
	}

	private static String simpleClassName(String fullClassName) {
		return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
	}

	private static String timeString() {
		return new SimpleDateFormat("HH:mm:ss").format(new Date());
	}

	private SOLogger() {
	}

}
