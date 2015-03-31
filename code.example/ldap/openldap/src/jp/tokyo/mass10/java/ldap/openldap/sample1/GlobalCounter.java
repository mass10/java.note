package jp.tokyo.mass10.java.ldap.openldap.sample1;

import java.util.HashMap;

final class GlobalCounter {

	private static final HashMap<String, Integer> _map = new HashMap<String, Integer>();

	private GlobalCounter() {

	}

	public static final void inc(String key) {

		Integer current = _map.get(key);
		if(current == null) {
			_map.put(key, 1);
		}
		else {
			_map.put(key, current + 1);
		}
	}

	public static final int current(String key) {

		final Integer current = _map.get(key);
		if(current == null)
			return 0;
		return current.intValue();
	}
}
