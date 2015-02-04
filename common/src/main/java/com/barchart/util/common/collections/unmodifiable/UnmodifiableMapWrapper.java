package com.barchart.util.common.collections.unmodifiable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UnmodifiableMapWrapper<K, V, W extends V> implements Map<K, V> {
	
	private final Map<K, W> map;
	
	public UnmodifiableMapWrapper(final Map<K, W> map) {
		this.map = map;
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
		return new UnmodifiableCollectionWrapper<V, W>(map.values());
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return new UnmodifiableEntrySetWrapper<K, V, W>(map.entrySet());
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

}
