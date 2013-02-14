package com.barchart.util.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class TestBlockingConcurrentHashMap {

	BlockingMap<String, String> map;

	@Before
	public void setUp() {
		map = new BlockingConcurrentHashMap<String, String>();
	}

	@Test
	public void testGetExists() throws Exception {
		map.put("key1", "value1");
		final String value = map.get("key1", 1, TimeUnit.SECONDS);
		assertEquals("value1", value);
	}

	@Test
	public void testGetWait() throws Exception {
		final long start = System.currentTimeMillis();
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(500);
				} catch (final InterruptedException e) {
				}
				map.put("key1", "value1");
			}
		}.start();
		final String value = map.get("key1", 1, TimeUnit.SECONDS);
		final long time = System.currentTimeMillis() - start;
		assertEquals("value1", value);
		assertTrue(time >= 500 && time <= 1000);
	}

	@Test
	public void testGetWaitFail() throws Exception {
		final long start = System.currentTimeMillis();
		final String value = map.get("key1", 1, TimeUnit.SECONDS);
		final long time = System.currentTimeMillis() - start;
		System.out.println(time);
		assertEquals(null, value);
		assertTrue(time >= 1000);
	}

}
