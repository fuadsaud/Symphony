package log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class InlineFormatter extends SimpleFormatter {

	private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

	@Override
	public String getHead(Handler h) {
		return System.lineSeparator();
	}

	@Override
	public String format(LogRecord record) {
		StringBuilder sb = new StringBuilder();

		// sb.append(record.getSequenceNumber() + " ");
		sb.append(formatter.format(new Date(record.getMillis())) + " ");

		sb.append("[" + record.getLevel() + "] ");
		sb.append("(" + record.getSourceClassName() + ":");
		sb.append(record.getSourceMethodName() + ") ");

		sb.append(record.getMessage() + System.lineSeparator());

		return sb.toString();
	}

	@Override
	public String getTail(Handler h) {
		return System.lineSeparator();
	}
}
