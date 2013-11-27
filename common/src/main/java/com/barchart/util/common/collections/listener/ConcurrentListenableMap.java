package com.barchart.util.common.collections.listener;

import java.util.concurrent.ConcurrentMap;

public interface ConcurrentListenableMap<K,V> extends ConcurrentMap<K,V>, ListenableMap<K,V> {

}
