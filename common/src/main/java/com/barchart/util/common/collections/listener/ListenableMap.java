package com.barchart.util.common.collections.listener;

public interface ListenableMap<K,V> {
	
	public void bindMapListener(MapListener<K,V> listener);

}
