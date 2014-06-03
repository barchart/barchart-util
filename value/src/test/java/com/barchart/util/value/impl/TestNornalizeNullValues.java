package com.barchart.util.value.impl;

import org.junit.Test;

import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.ValueFactory;

public class TestNornalizeNullValues {

	public static ValueFactory vals = ValueFactoryImpl.instance;

	private static final long TIMEOUT_MILLIS = 250;

	@Test(timeout = TIMEOUT_MILLIS)
	public void testNormalizeZeroPrice() {
		Price price = vals.newPrice(0.0);
		price.norm();
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testNormalizeZeroSize() {
		Size size = vals.newSize(0);
		size.norm();
	}

}
