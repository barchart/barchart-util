/**
 * 
 */
package com.barchart.util.values.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.barchart.util.math.DoubleParts;
import com.barchart.util.math.MathIEEE754;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.provider.ValueBuilder;

/**
 * @author g-litchfield
 * 
 */
public class TestValUtil {

	/**
	 * Test method for
	 * {@link com.barchart.base.util.values.util.ValUtil#asLong(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsLong() {
		final long l = 123456789l;
		final PriceValue pv = ValueBuilder.newPrice(l);
		assertEquals(l, ValUtil.asLong(pv));
	}

	/**
	 * Test method for
	 * {@link com.barchart.base.util.values.util.ValUtil#asInt(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsInt() {
		final int i = 123456;
		final PriceValue pv = ValueBuilder.newPrice(i);
		assertEquals(i, ValUtil.asInt(pv));
	}

	/**
	 * Test method for
	 * {@link com.barchart.base.util.values.util.ValUtil#asDouble(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsDouble() {
		final double d = 12345.6789;
		final DoubleParts dp = MathIEEE754.extractDecimal(d);
		final PriceValue pv = ValueBuilder.newPrice(dp.getMantissa(),
				dp.getExponent());
		assertEquals(d, ValUtil.asDouble(pv), 0.000000001);

	}

	/**
	 * Test method for
	 * {@link com.barchart.base.util.values.util.ValUtil#asFloat(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsFloat() {
		final float f = 12345.6789f;
		final DoubleParts dp = MathIEEE754.extractDecimal(f);
		final PriceValue pv = ValueBuilder.newPrice(dp.getMantissa(),
				dp.getExponent());
		assertEquals(f, ValUtil.asDouble(pv), 0.000000001);
	}

}
