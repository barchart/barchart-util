package com.barchart.util.cluster.hazelcast;

import java.util.concurrent.TimeUnit;

import com.hazelcast.core.HazelcastInstance;

public interface HazelcastCluster {

	/**
	 * Get the local hazelcast instance, blocking until it is initialized (or
	 * fails). If the node cannot be initialized, this method will return null.
	 */
	public HazelcastInstance getInstance();

	/**
	 * Get the local hazelcast instance, blocking for the specified time until a
	 * node is available. If the node is not available after the timeout
	 * expires, this method will return null.
	 */
	public HazelcastInstance getInstance(final long timeout, final TimeUnit unit)
			throws InterruptedException;

}
