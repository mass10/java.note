package tracing;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Trace {

	public static void put(Object... params) {

		final Exception e = new Exception();
		final StackTraceElement stack = e.getStackTrace()[1];
		final StringBuilder s = new StringBuilder();
		s.append(getTimestamp());
		s.append(" [TRACE] ");
		s.append("(");
		s.append(stack.getFileName());
		s.append(":");
		s.append(stack.getLineNumber());
		s.append(") ");
		for (final Object unknown : params) {
			s.append(unknown);
		}
		System.out.println(s);
	}

	private static String getTimestamp() {

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return format.format(Calendar.getInstance().getTime());
	}
}
