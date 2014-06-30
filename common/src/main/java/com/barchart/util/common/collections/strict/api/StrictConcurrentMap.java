package com.barchart.util.common.collections.strict.api;

import java.util.concurrent.ConcurrentMap;

/**
 * A concurrent map which throws an IllegalStateException in the following cases:
 * User attempts to add a null value    
 * User attempts to add a pre-existing key
 * User attempts to get or remove a non-existing key
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface StrictConcurrentMap<K, V> extends ConcurrentMap<K, V> {
	
	V replace(K key, V value);

}
