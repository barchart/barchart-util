package com.barchart.util.values.provider;

import org.junit.Test;

import com.barchart.util.values.api.PriceValue;

public class TestNormalizeNullPrices {

	private static final int TIMEOUT_MILLIS = 250;

	@Test(timeout = TIMEOUT_MILLIS)
	public void testNormalizeNullPrice() {
		ValueConst.NULL_PRICE.norm();
	}

	@Test(timeout = TIMEOUT_MILLIS)
	public void testNormalizeZeroPrice() {
		PriceValue price = ValueBuilder.newPrice(0.0d);
		price.norm();
	}

	@Test(timeout = TIMEOUT_MILLIS)
	public void testNormalizeNullDecimal() {
		ValueConst.NULL_DECIMAL.norm();
	}

	@Test(timeout = TIMEOUT_MILLIS)
	public void testNormalizeNullFraction() {
		ValueConst.NULL_FRACTION.norm();
	}

}
