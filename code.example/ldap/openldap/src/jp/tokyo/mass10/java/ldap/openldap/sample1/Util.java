package jp.tokyo.mass10.java.ldap.openldap.sample1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

final class Util {

	private Util() {

	}

	public static final String to_string(String s) throws Exception {

		return s == null ? "" : s;
	}

	private static final SimpleDateFormat full_timestamp_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static final String to_string(Date d) {

		if (d == null)
			return "";
		synchronized (full_timestamp_formatter) {
			return full_timestamp_formatter.format(d.getTime());
		}
	}

	public static final String to_string(Object unknown) throws Exception {

		if (unknown == null)
			return "";

		if (unknown instanceof Attribute)
			return to_string((Attribute) unknown);

		if (unknown instanceof String) {

			// final String conversion = read_time_format((String) unknown);
			// if(conversion != null && 0 < conversion.length())
			// return conversion;

			return (String) unknown;
		}

		if (unknown instanceof byte[]) {

			return to_string((byte[]) unknown);
		}

		return unknown.toString();
	}

	public static final String to_string(byte[] unknown) {

		final StringBuilder buffer = new StringBuilder();
		for (byte b : unknown) {
			buffer.append(String.format("%02x", b));
		}
		return buffer.toString();
	}

	public static final String read_time_format(String s) {

		final Date date = to_date(s);
		if (date == null) {
			return "";
		}

		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setLenient(true);
		return s + " (" + format.format(date) + " JST)";
	}

	public static final String left(String s, int size) {

		if (s == null) {
			return "";
		}

		final int length = s.length();
		if (length <= size) {
			return s;
		}

		return s.substring(0, size);
	}

	public static final String right(String s, int size) {

		if (s == null) {
			return "";
		}

		final int length = s.length();
		if (length <= size) {
			return s;
		}

		return s.substring(length - size);
	}

	public static final Date to_date(Object unknown) throws Exception {

		if (unknown == null)
			return null;
		if (unknown instanceof Attribute)
			return to_date((Attribute) unknown);
		if (unknown instanceof Date)
			return (Date) unknown;
		return to_date(unknown.toString());
	}

	public static final Date to_date(Attribute s) throws Exception {

		String x = to_string((Attribute) s);
		return to_date(x);
	}

	public static final Date to_date(String s) {

		if (s == null)
			return null;

		final int length = s.length();

		// [yyyyMMddHHmmssZ] LDAP UTC TIME VALUE
		if (length == 15 && is_digit(left(s, 14)) && "Z".equals(right(s, 1))) {

			try {

				final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				format.setLenient(true);

				// +0900 してからparse
				int year = parse_int(s.substring(0, 4));
				int month = parse_int(s.substring(4, 6));
				int day = parse_int(s.substring(6, 8));
				int hour = parse_int(s.substring(8, 10));
				int min = parse_int(s.substring(10, 12));
				int sec = parse_int(s.substring(12, 14));

				final String text = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour + 9, min, sec);

				return format.parse(text);
			}
			catch (Exception e) {
				return null;
			}
		}
		else {

		}

		return null;
	}

	private static final int parse_int(String s) {

		if (s == null || s.length() == 0)
			return 0;

		try {
			return Integer.parseInt(s);
		}
		catch (Exception e) {
			return 0;
		}
	}

	private static final boolean is_digit(char c) {

		return '0' <= c && c <= '9';
	}

	private static final boolean is_digit(String s) {

		if (s == null)
			return false;

		final int length = s.length();

		for (int i = 0; i < length; i++) {

			if (!is_digit(s.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	private static final boolean is_timestamp_type(String id) {

		return "modifyTimestamp".equalsIgnoreCase(id) || "createTimestamp".equalsIgnoreCase(id);
	}

	private static final Date utc_to_jst(Date date) {

		if (date == null)
			return null;

		final Calendar jst_calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
		jst_calendar.setTime(date);
		jst_calendar.add(Calendar.HOUR, 9);
		return jst_calendar.getTime();
	}

	@Deprecated
	public static final String describe_node_attribute(Attribute attribute) {

		try {
			final DirContext context = attribute.getAttributeDefinition();
			return context.getNameInNamespace();
		}
		catch (Exception e) {
			return e.getMessage();
		}
	}

	public static final String to_string(Attribute attribute) throws Exception {

		if (attribute == null)
			return "";

		// final String type_description = describe_node_attribute(attribute);

		final String field_type_id = attribute.getID();

		// ===================================================================
		// TIMESTAMP
		// ===================================================================
		if (is_timestamp_type(field_type_id)) {
			final String value = to_string(attribute.get());
			final SimpleDateFormat utc = new SimpleDateFormat("yyyyMMddHHmmss");
			utc.setLenient(true);
			final Date date = utc.parse(value);
			final SimpleDateFormat jst = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return value + " (" + jst.format(utc_to_jst(date)) + " JST)";
		}

		// ===================================================================
		// default
		// ===================================================================
		{
			final NamingEnumeration<?> e = attribute.getAll();
			final StringBuilder buffer = new StringBuilder();
			for (int i = 0; e.hasMoreElements(); i++) {
				final Object value = e.nextElement();
				if (i != 0) {
					buffer.append(", ");
				}
				buffer.append(to_string(value));
			}
			return buffer.toString();
		}
	}

	public static final Collection<String> sort(String[] en) {

		final TreeSet<String> names = new TreeSet<String>();
		for (String e : en) {
			names.add(e);
		}
		return names;
	}

	public static final Collection<String> sort(Enumeration<String> en) {

		final TreeSet<String> names = new TreeSet<String>();
		while (en.hasMoreElements()) {
			names.add(en.nextElement());
		}
		return names;
	}

	public static final String timestamp() {

		final Date value = Calendar.getInstance().getTime();
		return to_string(value);
	}

	public static final String timestamp(Date d) {

		return to_string(d);
	}

	public static final Object get_attribute(SearchResult e, String name) throws Exception {

		return e.getAttributes().get(name);
	}
}
