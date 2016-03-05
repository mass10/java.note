package jp.mass10.java.ldap.openldap.sample1;

import java.util.Collection;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

final class TimestampPrinter implements AbstractPrinter {

	public TimestampPrinter() {

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

	private static void dump_attributes(SearchResult node) throws Exception {

		if(node == null) {
			return;
		}

		final Attributes attributes = node.getAttributes();

		System.out.println("------------------------");

		for(final String id : _ids) {

			final Attribute value = attributes.get(id);

			System.out.print("  ");
			System.out.print(id);
			System.out.print(": ");
			System.out.print(Util.to_string(value));
			System.out.println();
		}
	}

	private static final Collection<String> _ids = Util.sort(new String[] { "mail", "uid", "createTimestamp", "modifyTimestamp" });
}