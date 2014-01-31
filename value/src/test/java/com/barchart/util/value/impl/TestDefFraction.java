package com.barchart.util.value.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.ValueFactory;

public class TestDefFraction {

	public static ValueFactory vals = new ValueFactoryImpl();
	
	public static Fraction BIN_Z00 = vals.newFraction(2, 0);
	public static Fraction BIN_N01 = vals.newFraction(2, -1);
	public static Fraction BIN_N02 = vals.newFraction(2, -2);
	public static Fraction BIN_N03 = vals.newFraction(2, -3);
	public static Fraction BIN_N04 = vals.newFraction(2, -4);
	public static Fraction BIN_N07 = vals.newFraction(2, -7);

	public static Fraction DEC_Z00 = vals.newFraction(10, 0);
	public static Fraction DEC_N01 = vals.newFraction(10, -1);
	public static Fraction DEC_N02 = vals.newFraction(10, -2);
	public static Fraction DEC_N03 = vals.newFraction(10, -3);
	public static Fraction DEC_N04 = vals.newFraction(10, -4);
	public static Fraction DEC_N05 = vals.newFraction(10, -5);
	public static Fraction DEC_N06 = vals.newFraction(10, -6);
	public static Fraction DEC_N07 = vals.newFraction(10, -7);

	@Test
	public void testPriceFraction() {

		Price price;
		long fraction;

		price = vals.newPrice(123500, -3);
		fraction = BIN_N01.priceFraction(price);
		assertEquals(1, fraction); // 1/2

		price = vals.newPrice(-123500, -3);
		fraction = BIN_N01.priceFraction(price);
		assertEquals(1, fraction); // 1/2

		price = vals.newPrice(123125, -3);
		fraction = BIN_N03.priceFraction(price);
		assertEquals(1, fraction); // 1/8

		price = vals.newPrice(1230250999, -6);
		fraction = BIN_N03.priceFraction(price);
		assertEquals(2, fraction); // 2/8, approximately

		price = vals.newPrice(123375, -3);
		fraction = BIN_N03.priceFraction(price);
		assertEquals(3, fraction); // 3/8

		price = vals.newPrice(12325, -2);
		fraction = BIN_N02.priceFraction(price);
		assertEquals(1, fraction); // 1/4

		price = vals.newPrice(12300, -2);
		fraction = BIN_N02.priceFraction(price);
		assertEquals(0, fraction); // 0/4

		price = vals.newPrice(1231875, -4);
		fraction = DEC_N04.priceFraction(price);
		assertEquals(1875, fraction); // 3/16

		price = vals.newPrice(1231875, -4);
		fraction = BIN_N04.priceFraction(price);
		assertEquals(3, fraction); // 3/16

		price = vals.newPrice(1237777, -4);
		fraction = BIN_N04.priceFraction(price);
		assertEquals(12, fraction); // 12/16 approximately

		price = vals.newPrice(999523437500L, -9);
		fraction = BIN_N07.priceFraction(price);
		assertEquals(67, fraction); // 67/128

		price = vals.newPrice(-999523437500L, -9);
		fraction = BIN_N07.priceFraction(price);
		assertEquals(67, fraction); // 67/128

		price = vals.newPrice(1, 0);
		fraction = BIN_Z00.priceFraction(price);
		assertEquals(0, fraction); // 0/1

		//

		price = vals.newPrice(1236998923, -6);
		fraction = DEC_N06.priceFraction(price);
		assertEquals(fraction, 998923);

		price = vals.newPrice(1236998923, -6);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 998);

		price = vals.newPrice(1236998923777L, -9);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 998); // truncated

		price = vals.newPrice(1236998923, 3);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 0);

		price = vals.newPrice(1, 0);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 0);

		price = vals.newPrice(1, 0);
		fraction = DEC_Z00.priceFraction(price);
		assertEquals(fraction, 0);

		price = vals.newPrice(-1, 0);
		fraction = DEC_Z00.priceFraction(price);
		assertEquals(fraction, 0);

	}

}
