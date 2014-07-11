package jp.tokyo.mar3.util;

import java.util.Calendar;

public final class Stopwatch {

	private long _start = _time();

	public Stopwatch() {

	}

	private static final long _time() {

		return Calendar.getInstance().getTimeInMillis();
	}

	@Override
	public String toString() {

		long since = this._start;
		long now = _time();

		long milliseconds = now - since;
		long seconds = 0;
		long minutes = 0;
		long hours = 0;

		while(1000 <= milliseconds) {
			seconds++;
			milliseconds -= 1000;
		}

		while(60 <= seconds) {
			minutes++;
			seconds -= 60;
		}

		while(60 <= minutes) {
			minutes -= 60;
			hours++;
		}

		return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);
	}

	public static void main(String[] args) throws Exception {

		final Stopwatch watch = new Stopwatch();

		Thread.sleep(12);

		System.out.println(watch);
	}
}
