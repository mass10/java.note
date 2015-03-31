package jp.tokyo.mass10.java.ldap.openldap.sample1;

import java.io.FileInputStream;
import java.util.Properties;

final class ConfigurationSettings {

	private final Properties _core = new Properties();

	private static final ConfigurationSettings _instance = new ConfigurationSettings();

	private ConfigurationSettings() {

	}

	public static ConfigurationSettings get_instance() throws Exception {

		return _instance;
	}

	public void configure() throws Exception {

		synchronized (this) {

			this._core.loadFromXML(new FileInputStream("settings.xml"));
		}
	}

	public void print() {

		synchronized (this) {

			System.out.println("$$$ configuration $$$");

			for (final Object key : this._core.keySet()) {

				System.out.print("  [");
				System.out.print(key);
				System.out.print("]=[");
				System.out.print(this._core.get(key));
				System.out.print("]");
				System.out.println();
			}
		}
	}

	public final String get_string(String key) {

		synchronized (this) {

			return this._core.getProperty(key);
		}
	}

	public final String get_string(String key, String default_value) {

		synchronized (this) {

			final String value = this._core.getProperty(key);
			if (value == null || value.length() == 0) {
				return default_value;
			}

			return value;
		}
	}

	public final AbstractPrinter get_printer_model() {

		String model_type_name = this.get_string("ldap.search.printer");
		if (model_type_name == null || model_type_name.length() == 0) {
			return new FullPrinter();
		}

		if (model_type_name.contains(".") == false) {
			model_type_name = this.getClass().getPackage().getName() + "." + model_type_name;
		}

		try {

			final Class<?> clazz = Class.forName(model_type_name);
			final Object instance = clazz.newInstance();
			if (!(instance instanceof AbstractPrinter)) {
				Logger.put("[" + instance.getClass().getName() + "] は認識できません。");
				return null;
			}

			return (AbstractPrinter) instance;
		}
		catch (Exception e) {
			e.printStackTrace();
			return new FullPrinter();
		}
	}
}
