package com.barchart.util.common.collections.listener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class ConcurrentListenableHashMap<K,V> extends ConcurrentHashMap<K,V> 
		implements ConcurrentListenableMap<K,V> {
	
	private final List<MapListener<K,V>> listeners = 
			new CopyOnWriteArrayList<MapListener<K,V>>();

	@Override
	public void bindMapListener(MapListener<K, V> listener) {
		listeners.add(listener);
	}

	@Override
	public V put(K key, V value) {
		for(MapListener<K,V> listener : listeners) {
			listener.onPut(key, value);
		}
		V temp = super.put(key, value);
		return temp;
	}

}
