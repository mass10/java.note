package jp.mass10.java.ldap.openldap.sample1;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

final class SimpleFileWriter {

	public SimpleFileWriter() {

	}

	public static final void put_line(String path, Object line) throws Exception {

		final FileOutputStream stream = new FileOutputStream(path, true);
		final OutputStreamWriter out = new OutputStreamWriter(stream, "utf-8");
		final BufferedWriter writer = new BufferedWriter(out);
		try {
			writer.write(line == null ? "" : line.toString());
			writer.newLine();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			writer.close();
			out.close();
			stream.close();
		}
	}
}