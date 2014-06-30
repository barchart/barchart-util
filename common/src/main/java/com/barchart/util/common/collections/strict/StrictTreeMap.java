package com.barchart.util.common.collections.strict;

import java.util.TreeMap;

import com.barchart.util.common.collections.strict.api.StrictNavigableMap;

/**
 * TreeMap superclass which throws an IllegalStateException in the following cases:
 * User attempts to add a null value    
 * User attempts to add a pre-existing key
 * User attempts to get or remove a non-existing key
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
@SuppressWarnings("serial")
public class StrictTreeMap<K, V> extends TreeMap<K, V> implements StrictNavigableMap<K, V> {
	
	private final Class<K> clazz;
	
	public StrictTreeMap(final Class<K> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public V replace(final K key, final V value) {
		
		if(!super.containsKey(key)) {
			throw new IllegalStateException("Unknown Key " + key.toString());
		} else {
			return super.put(key, value);
		}
		
	}

	@Override
	public V get(final Object key) {
		
		if(!clazz.isInstance(key)) {
			throw new IllegalStateException("Invalid key class - " + key.getClass().getCanonicalName());
		}
		
		if(!super.containsKey(key)) {
			throw new IllegalStateException("Unknown Key " + key.toString());
		}
		
		return super.get(key);
	}
	
	@Override
	public V remove(final Object key) {
		
		if(!clazz.isInstance(key)) {
			throw new IllegalStateException("Invalid key class - " + key.getClass().getCanonicalName());
		}
		
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
