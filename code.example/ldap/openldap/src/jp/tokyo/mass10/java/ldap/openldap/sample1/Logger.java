package jp.tokyo.mass10.java.ldap.openldap.sample1;

final class Logger {

	private Logger() {

	}

	public static void write_line(Object unknown) throws Exception {

		System.out.println(unknown);
		final String path = Util.timestamp().substring(0, 10) + ".log";
		SimpleFileWriter.put_line(path, unknown);
	}

	public static void write_line() throws Exception {

		write_line("");
	}

	public static void put(Object unknown) throws Exception {

		final StringBuilder line = new StringBuilder();
		line.append(Util.timestamp());
		line.append(" ");
		line.append(unknown);
		System.out.println(line);
		final String path = Util.timestamp().substring(0, 10) + ".log";
		SimpleFileWriter.put_line(path, line);
	}
}
