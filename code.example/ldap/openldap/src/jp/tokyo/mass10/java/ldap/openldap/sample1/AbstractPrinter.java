package jp.tokyo.mass10.java.ldap.openldap.sample1;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

interface AbstractPrinter {

	int dump(NamingEnumeration<SearchResult> result) throws Exception;
}
