package jp.tokyo.mass10.java.ldap.openldap.sample1;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

final class FullPrinter implements AbstractPrinter {

	public FullPrinter() {

	}

	@Override
	public int dump(NamingEnumeration<SearchResult> result) throws Exception {

		int affected = 0;

		for(; result != null && result.hasMoreElements();) {

			dump_attributes(result.nextElement());

			affected++;
		}

		return affected;
	}

	/**
	 * LDAPエントリーを単純にダンプします。
	 * 
	 * @param node
	 *            LDAPエントリー
	 * @throws Exception
	 */
	private static void dump_attributes(SearchResult node) throws Exception {

		if(node == null) {
			return;
		}

		final Attributes attributes = node.getAttributes();

		final NamingEnumeration<String> ids = attributes.getIDs();

		Logger.write_line("------------------------");

		for(final String id : Util.sort(ids)) {

			final Object value = attributes.get(id);

			final StringBuilder line = new StringBuilder();
			line.append("  ");
			line.append(id);
			line.append(": ");
			line.append(Util.to_string(value));

			Logger.write_line(line);
		}
	}
}