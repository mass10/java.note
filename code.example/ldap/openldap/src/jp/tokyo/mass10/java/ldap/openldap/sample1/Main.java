package jp.tokyo.mass10.java.ldap.openldap.sample1;

import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public final class Main {

	private Main() {

	}

	public static final void main(String[] args) throws Exception {

		final Stopwatch watch = new Stopwatch();
		try {
			Logger.put("### start ###");
			final ConfigurationSettings conf = ConfigurationSettings.get_instance();
			conf.configure();
			conf.print();
			select(conf.get_printer_model());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			Logger.put("処理時間: " + watch);
			Logger.put("Ok.");
		}
	}

	private static final void select(AbstractPrinter printer) throws Exception {

		final DirContext context = LdapConnectionFactory.get_ldap_connection();
		try {
			if (printer == null) {
				Logger.put("printer が null です。");
				return;
			}
			final ConfigurationSettings conf = ConfigurationSettings.get_instance();
			final String base_dn = conf.get_string("ldap.search.base");
			final String filter_string = conf.get_string("ldap.search.filter_string");
			final SearchControls controls = new SearchControls();
			controls.setReturningAttributes(new String[] { "*", "+" });
			controls.setSearchScope(1);
			final NamingEnumeration<SearchResult> result = context.search(base_dn, filter_string, controls);
			final int found = printer.dump(result);
			System.out.println();
			System.out.println(found + "件のノードを出力");
		}
		catch (Exception e) {
			throw e;
		}
		finally {
			context.close();
		}
	}
}