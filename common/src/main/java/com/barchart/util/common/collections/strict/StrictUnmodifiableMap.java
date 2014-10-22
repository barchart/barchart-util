package com.barchart.util.common.collections.strict;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.barchart.util.common.collections.strict.api.StrictMap;

/**
 * Wrapper around a StrictMap which prevents modifications.  Needed
 * because Collections.unmodifiableMap() returns a Map instead of a 
 * StrictMap. 
 *
 * @param <K>
 * @param <V>
 */
public class StrictUnmodifiableMap<K, V> implements StrictMap<K, V> {
	
	private final StrictMap<K, V> map;
	
	public StrictUnmodifiableMap(final StrictMap<K, V> map) {
		this.map = map;
	}

	@Override
	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public V replace(K key, V value) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(final Object value) {
		return map.containsValue(value);
	}

	@Override
	public V get(final Object key) {
		return map.get(key);
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

}
