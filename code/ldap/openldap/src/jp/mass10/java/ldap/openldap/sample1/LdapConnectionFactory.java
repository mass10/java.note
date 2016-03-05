package jp.mass10.java.ldap.openldap.sample1;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

final class LdapConnectionFactory {

	private LdapConnectionFactory() {

	}

	public static final DirContext get_ldap_connection() throws Exception {

		final ConfigurationSettings settings = ConfigurationSettings.get_instance();

		final String driver = settings.get_string("ldap.settings.driver");
		final String url = settings.get_string("ldap.settings.url");
		final String authentication_method = settings.get_string("ldap.settings.security_authentication", "simple");
		final String security_principal = settings.get_string("ldap.settings.security_principal");
		final String security_pass = settings.get_string("ldap.settings.security_credentials");

		final Hashtable<String, String> ldap_environment = new Hashtable<String, String>();

		ldap_environment.put(Context.INITIAL_CONTEXT_FACTORY, driver);
		ldap_environment.put(Context.PROVIDER_URL, url);
		ldap_environment.put(Context.SECURITY_AUTHENTICATION, authentication_method);
		ldap_environment.put(Context.SECURITY_PRINCIPAL, security_principal);
		ldap_environment.put(Context.SECURITY_CREDENTIALS, security_pass);

		if(false) {
			ldap_environment.put(Context.SECURITY_PROTOCOL, "ssl");
		}

		return new InitialDirContext(ldap_environment);
	}
}
