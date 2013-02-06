package com.barchart.util.values.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.PriceValue;

public class TestDefFraction {
	
	public static Fraction BIN_Z00 = newFraction(2, 0);
	public static Fraction BIN_N01 = newFraction(2, -1);
	public static Fraction BIN_N02 = newFraction(2, -2);
	public static Fraction BIN_N03 = newFraction(2, -3);
	public static Fraction BIN_N04 = newFraction(2, -4);
	public static Fraction BIN_N07 = newFraction(2, -7);
	
	public static Fraction DEC_Z00 = newFraction(10, 0);
	public static Fraction DEC_N01 = newFraction(10, -1);
	public static Fraction DEC_N02 = newFraction(10, -2);
	public static Fraction DEC_N03 = newFraction(10, -3);
	public static Fraction DEC_N04 = newFraction(10, -4);
	public static Fraction DEC_N05 = newFraction(10, -5);
	public static Fraction DEC_N06 = newFraction(10, -6);
	public static Fraction DEC_N07 = newFraction(10, -7);
	
	@Test
	public void testPriceFraction() {
		
		PriceValue price;
		long fraction;

		price = newPrice(123500, -3);
		fraction = BIN_N01.priceFraction(price);
		assertEquals(1, fraction); // 1/2

		price = newPrice(-123500, -3);
		fraction = BIN_N01.priceFraction(price);
		assertEquals(1, fraction); // 1/2

		price = newPrice(123125, -3);
		fraction = BIN_N03.priceFraction(price);
		assertEquals(1, fraction); // 1/8

		price = newPrice(1230250999, -6);
		fraction = BIN_N03.priceFraction(price);
		assertEquals(2, fraction); // 2/8, approximately

		price = newPrice(123375, -3);
		fraction = BIN_N03.priceFraction(price);
		assertEquals(3, fraction); // 3/8

		price = newPrice(12325, -2);
		fraction = BIN_N02.priceFraction(price);
		assertEquals(1, fraction); // 1/4

		price = newPrice(12300, -2);
		fraction =  BIN_N02.priceFraction(price);
		assertEquals(0, fraction); // 0/4

		price = newPrice(1231875, -4);
		fraction = DEC_N04.priceFraction(price);
		assertEquals(1875, fraction); // 3/16

		price = newPrice(1231875, -4);
		fraction = BIN_N04.priceFraction(price);
		assertEquals(3, fraction); // 3/16

		price = newPrice(1237777, -4);
		fraction = BIN_N04.priceFraction(price);
		assertEquals(12, fraction); // 12/16 approximately

		price = newPrice(999523437500L, -9);
		fraction = BIN_N07.priceFraction(price);
		assertEquals(67, fraction); // 67/128

		price = newPrice(-999523437500L, -9);
		fraction = BIN_N07.priceFraction(price);
		assertEquals(67, fraction); // 67/128

		price = newPrice(1, 0);
		fraction = BIN_Z00.priceFraction(price);
		assertEquals(0, fraction); // 0/1

		//

		price = newPrice(1236998923, -6);
		fraction = DEC_N06.priceFraction(price);
		assertEquals(fraction, 998923);

		price = newPrice(1236998923, -6);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 998);

		price = newPrice(1236998923777L, -9);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 998); // truncated

		price = newPrice(1236998923, 3);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 0);

		price = newPrice(1, 0);
		fraction = DEC_N03.priceFraction(price);
		assertEquals(fraction, 0);

		price = newPrice(1, 0);
		fraction = DEC_Z00.priceFraction(price);
		assertEquals(fraction, 0);

		price = newPrice(-1, 0);
		fraction = DEC_Z00.priceFraction(price);
		assertEquals(fraction, 0);

		
	}

}
