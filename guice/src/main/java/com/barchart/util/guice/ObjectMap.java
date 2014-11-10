package com.barchart.util.guice;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Key;

final class ObjectMap {

	private Map<Key<?>, Object> store = new HashMap<Key<?>, Object>();

	@SuppressWarnings("unchecked")
	public <T> T get(Key<T> key) {
		return (T) store.get(key);
	}

	public <T> void set(Key<T> key, T instance) {
		store.put(key, instance);
	}

}
