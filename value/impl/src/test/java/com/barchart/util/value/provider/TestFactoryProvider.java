package com.barchart.util.value.provider;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.FactoryLoader;

public class TestFactoryProvider {

	@Test
	public void loader() throws Exception {
		final Factory factory = FactoryLoader.load();
		assertNotNull(factory);
	}

}
