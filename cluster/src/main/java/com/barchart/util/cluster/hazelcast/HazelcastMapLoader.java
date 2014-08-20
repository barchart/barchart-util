package com.barchart.util.cluster.hazelcast;

import com.hazelcast.core.MapLoader;

public interface HazelcastMapLoader<V> extends MapLoader<String, V> {

	public String type();

}
