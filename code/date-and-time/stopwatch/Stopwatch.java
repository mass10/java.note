
public final class Stopwatch {

	private long _start = _time();

	public Stopwatch() {

	}

	private static final long _time() {

		return System.currentTimeMillis();
	}

	@Override
	public String toString() {

		long milliseconds = _time() - this._start;

		return toString(milliseconds);
	}

	private static String toString(long milliseconds) {

		long seconds = milliseconds / 1000;
		milliseconds = milliseconds % 1000;

		long minutes = seconds / 60;
		seconds = seconds % 60;

		long hours = minutes / 60;
		minutes = minutes % 60;

		return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
	}
}