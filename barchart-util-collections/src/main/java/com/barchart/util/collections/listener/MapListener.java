package com.barchart.util.collections.listener;

public interface MapListener<K,V> {

	public void onPut(K k, V v);
	
}
