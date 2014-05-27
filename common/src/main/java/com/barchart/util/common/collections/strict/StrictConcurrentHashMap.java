package com.barchart.util.common.collections.strict;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashmap superclass which throws an IllegalStateException in the following cases:
 * User attempts to add a null value    
 * User attempts to add a pre-existing key
 * User attempts to get or remove a non-existing key
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
@SuppressWarnings("serial")
public class StrictConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> 
		implements StrictConcurrentMap<K, V> {

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
	
	@Override
	public V put(final K key, final V value) {
		
		if(value == null) {
			throw new IllegalStateException("Value cannot be null");
		}
		
		if(super.containsKey(key)) {
			throw new IllegalStateException("Key " + key.toString() + " already in map");
		}
		
		return super.put(key, value);
	}
	
}
