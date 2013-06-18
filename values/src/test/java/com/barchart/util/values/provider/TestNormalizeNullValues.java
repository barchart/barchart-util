package com.barchart.util.values.provider;

import org.junit.Test;

import com.barchart.util.values.api.PriceValue;

public class TestNormalizeNullValues {
	
	private static final long TIMEOUT_MILLIS = 250;
	
	@Test(timeout = TIMEOUT_MILLIS) 
	public void testNormalizeNullPrice() {
		PriceValue price = ValueConst.NULL_PRICE;
		price.norm();
	}

	@Test(timeout = TIMEOUT_MILLIS)
	public void testNormalizeZeroPrice() {
		PriceValue price = ValueBuilder.newPrice(0, 0);
		price.norm();
	}
	
}
