package jp.mass10.java.ldap.openldap.sample1;

import java.util.TreeSet;

@SuppressWarnings("serial")
final class GlobalTreeSet extends TreeSet<String> {

	private static final GlobalTreeSet _instance = new GlobalTreeSet();

	private GlobalTreeSet() {

	}

	@SuppressWarnings("unused")
	private static final GlobalTreeSet get_instance() {
		return _instance;
	}
}
