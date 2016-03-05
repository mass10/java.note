package jp.mass10.java.ldap.openldap.sample1;

import java.util.HashMap;
import java.util.Map;

final class GlobalCache {

	private static final Map<String, Object> _cache = new HashMap<String, Object>();

	private GlobalCache() {

	}

	public static Object find(String key) {

		synchronized (_cache) {
			return _cache.get(key);
		}
	}

	public static final void store(String key, Object unknown) {

		synchronized (_cache) {
			_cache.put(key, unknown);
		}
	}
}
