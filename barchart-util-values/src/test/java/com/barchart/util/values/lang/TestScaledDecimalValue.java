/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.lang;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.values.api.PriceValue;

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
		PriceValue p1 = newPrice(1000, 2);
		PriceValue p2 = newPrice(1000, 2);
		assertEquals(p1.compareTo(p2), 0);
	}

	@Test
	public void TestCompare2() {
		PriceValue p1 = newPrice(1000, 2);
		PriceValue p2 = newPrice(1001, 2);
		assertEquals(p1.compareTo(p2), -1);
	}

	@Test
	public void TestCompare3() {
		PriceValue p1 = newPrice(1001, 2);
		PriceValue p2 = newPrice(1000, 2);
		assertEquals(p1.compareTo(p2), +1);
	}

	@Test
	public void TestCompare4() {
		PriceValue p1 = newPrice(100100, 2);
		PriceValue p2 = newPrice(1001, 4);
		assertEquals(p1.compareTo(p2), 0);
	}

	@Test
	public void TestCompare5() {
		PriceValue p1 = newPrice(Long.MAX_VALUE, 2);
		PriceValue p2 = newPrice(Long.MIN_VALUE / 1000, 4);
		assertEquals(p1.compareTo(p2), 1);
	}

	@Test
	public void TestCompare6() {
		PriceValue p1 = newPrice(Long.MAX_VALUE, 2);
		PriceValue p2 = newPrice(Long.MIN_VALUE / 10, 4);
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
		PriceValue p1 = newPrice(100100, 2);
		PriceValue p2 = p1.scale(0);
		// System.err.println("p1 : " + p1 + " p2 : " + p2);
		assertTrue(p1.equals(p2));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestToDouble() {

		PriceValue p1 = newPrice(100100, -2);

		double f1 = p1.asDouble();

		assertEquals(f1, 1001.0, ERR);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestAdd() {

		PriceValue p1 = newPrice(9710013123L, -2);
		PriceValue p2 = newPrice(101298634, -3);
		PriceValue p3 = p1.add(p2);

		double d1 = p1.asDouble();
		double d2 = p2.asDouble();
		double d3 = p3.asDouble();

		assertEquals(d3, d1 + d2, ERR);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestSub() {

		PriceValue p1 = newPrice(13123, -2);
		PriceValue p2 = newPrice(9712986342L, -4);
		PriceValue p3 = p1.sub(p2);

		double d1 = p1.asDouble();
		double d2 = p2.asDouble();
		double d3 = p3.asDouble();

		assertEquals(d3, d1 - d2, 0.00001);

	}

	@Test(expected = ArithmeticException.class)
	public void TestSubOverflow1() {

		PriceValue p1 = newPrice(Long.MAX_VALUE, -2);
		PriceValue p2 = newPrice(Long.MAX_VALUE, -4);

		PriceValue p3 = p1.sub(p2);

		System.err.println("p1 : " + p1);
		System.err.println("p2 : " + p2);
		System.err.println("p3 : " + p3);
	}

	@Test(expected = ArithmeticException.class)
	public void TestSubOverflow2() {

		PriceValue p1 = newPrice(-10L, 0);
		PriceValue p2 = newPrice(Long.MAX_VALUE, 0);

		PriceValue p3 = p1.sub(p2);

		System.err.println("p1 : " + p1);
		System.err.println("p2 : " + p2);
		System.err.println("p3 : " + p3);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestMult1() {

		int factor = 124923655;
		PriceValue p1 = newPrice(9710013123L, -2);
		PriceValue p2 = p1.mult(factor);

		double d1 = p1.asDouble();
		double d2 = p2.asDouble();

		assertEquals(d2, d1 * factor, ERR);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestCount1() {

		PriceValue p1 = newPrice(1112131415, -8);
		PriceValue p2 = newPrice(1112131415, -8);

		long count = p1.count(p2);

		double d1 = p1.asDouble();

		double d2 = p2.asDouble();

		assertEquals(d1 / d2, count, ERR);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void TestCount2() {

		PriceValue p1 = newPrice(1112131415, -4);
		PriceValue p2 = newPrice(1112131415, -8);

		long count = p1.count(p2);

		double d1 = p1.asDouble();

		double d2 = p2.asDouble();

		assertEquals(d1 / d2, count, ERR_09);

	}

	@Test(expected = ArithmeticException.class)
	public void TestMultOverflow1() {

		PriceValue p1 = newPrice(Long.MAX_VALUE, 0);

		p1.mult(Long.MAX_VALUE);

	}

	@Test(expected = ArithmeticException.class)
	public void TestAddOverflow1() {

		PriceValue p1 = newPrice(Long.MAX_VALUE / 20, -123);

		PriceValue p2 = newPrice(-Long.MAX_VALUE / 20, -125);

		p1.add(p2);

	}

	@Test(expected = ArithmeticException.class)
	public void TestAddOverflow2() {

		PriceValue p1 = newPrice(Long.MAX_VALUE / 2, 0);

		PriceValue p2 = newPrice(Long.MAX_VALUE, 0);

		p1.add(p2);

	}

}
