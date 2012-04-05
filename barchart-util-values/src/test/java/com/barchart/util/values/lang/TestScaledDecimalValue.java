/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.lang;

import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.util.ValUtil;

public class TestScaledDecimalValue {

	static final int K = 1000;

	static final double ERR = Double.MIN_VALUE;

	static final double ERR_09 = 1.0 / (3 * K);

	static final double ERR_12 = 1.0 / (4 * K);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void TestCompare1() {
		final PriceValue p1 = newPrice(1000, 2);
		final PriceValue p2 = newPrice(1000, 2);
		assertEquals(p1.compareTo(p2), 0);
	}

	@Test
	public void TestCompare2() {
		final PriceValue p1 = newPrice(1000, 2);
		final PriceValue p2 = newPrice(1001, 2);
		assertEquals(p1.compareTo(p2), -1);
	}

	@Test
	public void TestCompare3() {
		final PriceValue p1 = newPrice(1001, 2);
		final PriceValue p2 = newPrice(1000, 2);
		assertEquals(p1.compareTo(p2), +1);
	}

	@Test
	public void TestCompare4() {
		final PriceValue p1 = newPrice(100100, 2);
		final PriceValue p2 = newPrice(1001, 4);
		assertEquals(p1.compareTo(p2), 0);
	}

	@Test
	public void TestCompare5() {
		final PriceValue p1 = newPrice(Long.MAX_VALUE, 2);
		final PriceValue p2 = newPrice(Long.MIN_VALUE / 1000, 4);
		assertEquals(p1.compareTo(p2), 1);
	}

	@Test
	public void TestCompare6() {
		final PriceValue p1 = newPrice(Long.MAX_VALUE, 2);
		final PriceValue p2 = newPrice(Long.MIN_VALUE / 10, 4);
		assertEquals(p1.compareTo(p2), 1);
	}

	@Test
	public void TestCompare7() {

		PriceValue p1 = newPrice(Long.MAX_VALUE / 2, 2);
		PriceValue p2 = newPrice(Long.MAX_VALUE / 3, 4);

		p1 = newPrice(Long.MAX_VALUE / 2, 2);
		p2 = newPrice(Long.MAX_VALUE / 3, 4);
		assertEquals(p1.compareTo(p2), -1);

		p1 = newPrice(Long.MAX_VALUE / 2, 2);
		p2 = newPrice(Long.MAX_VALUE / 3, 2);
		assertEquals(p1.compareTo(p2), 1);

	}

	@Test
	public void TestNormal() {
		final PriceValue p1 = newPrice(100100, 2);
		final PriceValue p2 = p1.scale(0);
		// System.err.println("p1 : " + p1 + " p2 : " + p2);
		assertTrue(p1.equals(p2));
	}

	@Test
	public void TestToDouble() {

		final PriceValue p1 = newPrice(100100, -2);

		final double f1 = ValUtil.asDouble(p1);

		assertEquals(f1, 1001.0, ERR);

	}

	@Test
	public void TestAdd() {

		final PriceValue p1 = newPrice(9710013123L, -2);
		final PriceValue p2 = newPrice(101298634, -3);
		final PriceValue p3 = p1.add(p2);

		final double d1 = ValUtil.asDouble(p1);
		final double d2 = ValUtil.asDouble(p2);
		final double d3 = ValUtil.asDouble(p3);

		assertEquals(d3, d1 + d2, ERR);

	}

	@Test
	public void TestSub() {

		final PriceValue p1 = newPrice(13123, -2);
		final PriceValue p2 = newPrice(9712986342L, -4);
		final PriceValue p3 = p1.sub(p2);

		final double d1 = ValUtil.asDouble(p1);
		final double d2 = ValUtil.asDouble(p2);
		final double d3 = ValUtil.asDouble(p3);

		assertEquals(d3, d1 - d2, 0.00001);

	}

	@Test(expected = ArithmeticException.class)
	public void TestSubOverflow1() {

		final PriceValue p1 = newPrice(Long.MAX_VALUE, -2);
		final PriceValue p2 = newPrice(Long.MAX_VALUE, -4);

		final PriceValue p3 = p1.sub(p2);

		System.err.println("p1 : " + p1);
		System.err.println("p2 : " + p2);
		System.err.println("p3 : " + p3);
	}

	@Test(expected = ArithmeticException.class)
	public void TestSubOverflow2() {

		final PriceValue p1 = newPrice(-10L, 0);
		final PriceValue p2 = newPrice(Long.MAX_VALUE, 0);

		final PriceValue p3 = p1.sub(p2);

		System.err.println("p1 : " + p1);
		System.err.println("p2 : " + p2);
		System.err.println("p3 : " + p3);

	}

	@Test
	public void TestMult1() {

		final int factor = 124923655;
		final PriceValue p1 = newPrice(9710013123L, -2);
		final PriceValue p2 = p1.mult(factor);

		final double d1 = ValUtil.asDouble(p1);
		final double d2 = ValUtil.asDouble(p2);

		assertEquals(d2, d1 * factor, ERR);

	}

	@Test
	public void TestCount1() {

		final PriceValue p1 = newPrice(1112131415, -8);
		final PriceValue p2 = newPrice(1112131415, -8);

		final long count = p1.count(p2);

		final double d1 = ValUtil.asDouble(p1);

		final double d2 = ValUtil.asDouble(p2);

		assertEquals(d1 / d2, count, ERR);

	}

	@Test
	public void TestCount2() {

		final PriceValue p1 = newPrice(1112131415, -4);
		final PriceValue p2 = newPrice(1112131415, -8);

		final long count = p1.count(p2);

		final double d1 = ValUtil.asDouble(p1);

		final double d2 = ValUtil.asDouble(p2);

		assertEquals(d1 / d2, count, ERR_09);

	}

	@Test(expected = ArithmeticException.class)
	public void TestMultOverflow1() {

		final PriceValue p1 = newPrice(Long.MAX_VALUE, 0);

		p1.mult(Long.MAX_VALUE);

	}

	@Test(expected = ArithmeticException.class)
	public void TestAddOverflow1() {

		final PriceValue p1 = newPrice(Long.MAX_VALUE / 20, -123);

		final PriceValue p2 = newPrice(-Long.MAX_VALUE / 20, -125);

		p1.add(p2);

	}

	@Test(expected = ArithmeticException.class)
	public void TestAddOverflow2() {

		final PriceValue p1 = newPrice(Long.MAX_VALUE / 2, 0);

		final PriceValue p2 = newPrice(Long.MAX_VALUE, 0);

		p1.add(p2);

	}

}
