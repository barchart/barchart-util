package com.barchart.util.common.collections.expectant;

import java.util.TreeMap;

/**
 * Treemap superclass which throws IllegalStateException instead of returning
 * null for missing keys.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
@SuppressWarnings("serial")
public class ExpectantTreeMap<K, V> extends TreeMap<K, V> implements ExcpectantNavigableMap<K, V> {

	@Override
	public V get(Object key) {
		
		if(!super.containsKey(key)) {
			throw new IllegalStateException("Unknown Key " + key.toString());
		}
		
		return super.get(key);
	}
	
	@Override
	public V remove(Object key) {
		
		if(!super.containsKey(key)) {
			throw new IllegalStateException("Unknown Key " + key.toString());
		}
		
		return super.remove(key);
	}
	
}