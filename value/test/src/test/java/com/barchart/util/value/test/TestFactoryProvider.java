package com.barchart.util.value.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;

public class TestFactoryProvider {

	public static void main(final String... args) throws Exception {
		new TestFactoryProvider().loader();
	}

	@Test
	public void loader() throws Exception {
		final Factory factory = FactoryLoader.load();
		assertNotNull(factory);
	}

}
