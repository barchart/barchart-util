package com.barchart.util.cluster.hazelcast;

import com.hazelcast.core.MapLoader;

public interface HazelcastStoreRegistry {

	public MapLoader<String, ?> getStore(String id);

}
