/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
/**
 * 
 */
package temp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.math.DoubleParts;
import com.barchart.util.math.MathIEEE754;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.impl.ValueBuilder;

/**
 * @author g-litchfield
 * 
 */
public class TestValueUtil {

	/**
	 * Test method for
	 * {@link com.barchart.ValueUtil.util.values.util.ValUtil#asLong(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsLong() {
		final long l = 123456789l;
		final Price pv = ValueBuilder.newPrice(l);
		assertEquals(l, ValueUtil.asLong(pv));
	}

	/**
	 * Test method for
	 * {@link com.barchart.ValueUtil.util.values.util.ValUtil#asInt(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsInt() {
		final int i = 123456;
		final Price pv = ValueBuilder.newPrice(i);
		assertEquals(i, ValueUtil.asInt(pv));
	}

	/**
	 * Test method for
	 * {@link com.barchart.ValueUtil.util.values.util.ValUtil#asDouble(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsDouble() {
		final double d = 12345.6789;
		final DoubleParts dp = MathIEEE754.extractDecimal(d);
		final Price pv = ValueBuilder.newPrice(dp.getMantissa(),
				dp.getExponent());
		assertEquals(d, ValueUtil.asDouble(pv), 0.000000001);

	}

	/**
	 * Test method for
	 * {@link com.barchart.ValueUtil.util.values.util.ValUtil#asFloat(com.barchart.util.values.lang.ScaledDecimal)}
	 * .
	 */
	@Test
	public void testAsFloat() {
		final float f = 12345.6789f;
		final DoubleParts dp = MathIEEE754.extractDecimal(f);
		final Price pv = ValueBuilder.newPrice(dp.getMantissa(),
				dp.getExponent());
		assertEquals(f, ValueUtil.asDouble(pv), 0.000000001);
	}

}
