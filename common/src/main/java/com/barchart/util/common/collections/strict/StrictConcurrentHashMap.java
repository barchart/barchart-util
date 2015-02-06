package com.barchart.util.common.collections.strict;

import java.util.concurrent.ConcurrentHashMap;

import com.barchart.util.common.collections.strict.api.StrictConcurrentMap;

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
	
	private final Class<K> clazz;
	
	public StrictConcurrentHashMap(final Class<K> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public V replace(final K key, final V value) {
		V previousValue = super.put(key, value);
		if (previousValue != null) {
			super.put(key, previousValue);
			throw new IllegalStateException("Unknown Key " + key.toString());
		} else {
			return previousValue;
		}
	}

	@Override
	public V get(final Object key) {
		
		if(!clazz.isInstance(key)) {
			throw new IllegalStateException("Invalid key class - " + key.getClass().getCanonicalName());
		}
		
		V value = super.get(key);
		if (value == null) {
			throw new IllegalStateException("Unknown Key " + key.toString());
		}
		
		return value;
	}
	
	@Override
	public V remove(final Object key) {
		
		if(!clazz.isInstance(key)) {
			throw new IllegalStateException("Invalid key class - " + key.getClass().getCanonicalName());
		}
		
		V value = super.remove(key);
		if (value == null) {
			throw new IllegalStateException("Unknown Key " + key.toString());
		}
		
		return value;
	}
	
	@Override
	public V put(final K key, final V value) {
		
		if(value == null) {
			throw new IllegalStateException("Value cannot be null");
		}
		
		V previousValue = super.putIfAbsent(key, value);
		if (previousValue != null) {
			throw new IllegalStateException("Key " + key.toString() + " already in map");
		} else {
			return null;
		}
	}
	
}
