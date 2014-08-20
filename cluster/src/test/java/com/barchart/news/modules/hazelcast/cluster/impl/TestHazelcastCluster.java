package com.barchart.news.modules.hazelcast.cluster.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.cluster.hazelcast.HazelcastStoreRegistry;
import com.barchart.util.cluster.hazelcast.impl.HazelcastClusterProvider;
import com.barchart.util.test.concurrent.CallableTest;
import com.barchart.util.test.osgi.ComponentUtil;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStore;

public class TestHazelcastCluster {

	/*
	 * Merged to one giant test because hazelcast cluster takes forever to
	 * setup.
	 */
	@Test(expected = IllegalStateException.class)
	public void test() throws Exception {

		// testClusterJoin
		CallableTest.waitFor(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				return cl1.getInstance().getCluster().getMembers().size() == 3;
			}
		}, 30000);
		assertEquals(3, cl1.getInstance().getCluster().getMembers().size());

		// testData
		final byte[] data = new byte[] { 1, 2, 3 };
		cl1.getInstance().getMap("testCache").put("KEY", data);
		final byte[] retrieved = (byte[]) cl2.getInstance().getMap("testCache")
				.get("KEY");

		assertArrayEquals(data, retrieved);

		// testEviction
		final List<String> ids = new ArrayList<String>();
		for (int i = 0; i < 150; i++) {
			final byte[] data2 = new byte[] { (byte) i };
			final String id = String.valueOf(i);
			ids.add(id);
			cl1.getInstance().getMap("testCache").put(id, data2);
			Thread.sleep(1);
		}

		Thread.sleep(1000);
		final int count = cl1.getInstance().getMap("testCache").size();
		assertTrue(count < 150);
		assertNull(cl2.getInstance().getMap("testCache").get(ids.get(0)));
		assertNotNull(cl2.getInstance().getMap("testCache")
				.get(ids.get(ids.size() - 1)));

		// testClusterDistribution
		final int half = count / 2;
		IMap<String, byte[]> map = cl1.getInstance().getMap("testCache");
		assertTrue(map.getLocalMapStats().getOwnedEntryCount() < half);
		assertTrue(map.getLocalMapStats().getBackupEntryCount() > half);

		map = cl2.getInstance().getMap("testCache");
		assertTrue(map.getLocalMapStats().getOwnedEntryCount() < half);
		assertTrue(map.getLocalMapStats().getBackupEntryCount() > half);

		map = cl3.getInstance().getMap("testCache");
		assertTrue(map.getLocalMapStats().getOwnedEntryCount() < half);
		assertTrue(map.getLocalMapStats().getBackupEntryCount() > half);

		// testInitialLoad
		map = cl3.getInstance().getMap("loadCache");
		assertEquals(1, map.size());

		// testLoadFromStore
		map = cl3.getInstance().getMap("loadCache2");
		assertEquals(0, map.size());
		final byte[] loaded = map.get("1");
		assertNotNull(loaded);
		assertArrayEquals(new byte[] { 1 }, loaded);

		// testSaveToStore
		assertEquals(0, store.data.size());
		map = cl3.getInstance().getMap("storeCache");
		map.put("1", new byte[] { 1 });
		assertEquals(1, store.data.size());
		assertArrayEquals(new byte[] { 1 }, store.data.get("1"));

		// testDeleteFromStore
		map.remove("1");
		assertEquals(0, store.data.size());

		// testMapInvalid
		ComponentUtil.deactivate(cl3);
		map.get("1");

	}

	// @Test
	public void testAdapterFailure() throws Exception {
		final HazelcastClusterProvider cp = new HazelcastClusterProvider();
		ComponentUtil.activate(cp, getConfig("cp"));
		assertNotNull(cp.getInstance());
	}

	HazelcastClusterProvider cl1;
	HazelcastClusterProvider cl2;
	HazelcastClusterProvider cl3;
	TestLoader loader;
	TestLoader loader2;
	TestStore store;

	@Before
	public void setUp() throws Exception {

		final TestStoreRegistry registry = new TestStoreRegistry();

		loader = new TestLoader(true);
		loader.data.put("1", new byte[] { 1 });
		loader2 = new TestLoader(false);
		loader2.data.put("1", new byte[] { 1 });
		store = new TestStore(false);

		registry.put("loader", loader);
		registry.put("loader2", loader2);
		registry.put("store", store);

		// Balls this is slow

		cl1 = new HazelcastClusterProvider();
		ComponentUtil.bind(cl1, registry, HazelcastStoreRegistry.class);
		ComponentUtil.activate(cl1, getConfig("c1"));

		Thread.sleep(100);

		cl2 = new HazelcastClusterProvider();
		ComponentUtil.bind(cl2, registry, HazelcastStoreRegistry.class);
		ComponentUtil.activate(cl2, getConfig("c2"));

		Thread.sleep(100);

		cl3 = new HazelcastClusterProvider();
		ComponentUtil.bind(cl3, registry, HazelcastStoreRegistry.class);
		ComponentUtil.activate(cl3, getConfig("c3"));

	}

	private String getConfig(final String name) {

		return "{ " + //
				"name = \"" + name + "\", " + //
				"cluster-name = \"" + getClass().getName() + "\", " + //
				"backup-count = 2, " + //
				"adapter-timeout = 2s, " + //
				"maps = [ " + //
				"  {" + //
				"    name = testCache, " + //
				"    size = 100, " + //
				"    eviction = LRU " + //
				"  }, " + //
				"  {" + //
				"    name = loadCache, " + //
				"    store = loader " + //
				"  }, " + //
				"  {" + //
				"    name = loadCache2, " + //
				"    store = loader2 " + //
				"  }, " + //
				"  {" + //
				"    name = storeCache, " + //
				"    store = store " + //
				"  }, " + //
				" ]" + //
				"}";

	}

	@After
	public void tearDown() throws Exception {
		ComponentUtil.deactivate(cl1);
		ComponentUtil.deactivate(cl2);
		ComponentUtil.deactivate(cl3);
	}

	private static class TestStoreRegistry extends
			HashMap<String, MapLoader<String, byte[]>> implements
			HazelcastStoreRegistry {

		@Override
		public MapLoader<String, ?> getStore(final String id) {
			return get(id);
		}

	}

	private static class TestLoader implements MapLoader<String, byte[]> {

		protected final Map<String, byte[]> data = new HashMap<String, byte[]>();

		private final boolean loadAll;

		public TestLoader(final boolean loadAll_) {
			loadAll = loadAll_;
		}

		@Override
		public byte[] load(final String key) {
			return data.get(key);
		}

		@Override
		public Map<String, byte[]> loadAll(final Collection<String> keys) {
			final Map<String, byte[]> map = new HashMap<String, byte[]>();
			for (final String key : keys) {
				map.put(key, load(key));
			}
			return map;
		}

		@Override
		public Set<String> loadAllKeys() {
			if (loadAll) {
				return new HashSet<String>() {
					{
						add("1");
					}
				};
			} else {
				return null;
			}
		}

	}

	private class TestStore extends TestLoader implements
			MapStore<String, byte[]> {

		public TestStore(final boolean loadAll_) {
			super(loadAll_);
		}

		@Override
		public void store(final String key, final byte[] value) {
			data.put(key, value);
		}

		@Override
		public void storeAll(final Map<String, byte[]> map) {
			data.putAll(map);
		}

		@Override
		public void delete(final String key) {
			data.remove(key);
		}

		@Override
		public void deleteAll(final Collection<String> keys) {
			data.keySet().removeAll(keys);
		}
	}

}
