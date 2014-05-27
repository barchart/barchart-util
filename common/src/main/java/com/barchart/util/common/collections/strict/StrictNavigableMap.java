package com.barchart.util.common.collections.strict;

import java.util.NavigableMap;

/**
 * A navigable map which throws an IllegalStateException in the following cases:
 * User attempts to add a null value    
 * User attempts to add a pre-existing key
 * User attempts to get or remove a non-existing key
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface StrictNavigableMap<K, V> extends NavigableMap<K, V> {

}
