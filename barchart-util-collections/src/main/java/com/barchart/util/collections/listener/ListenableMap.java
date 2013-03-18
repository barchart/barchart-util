package com.barchart.util.collections.listener;

public interface ListenableMap<K,V> {
	
	public void bindMapListener(MapListener<K,V> listener);

}
