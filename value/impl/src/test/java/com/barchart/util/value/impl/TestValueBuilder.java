/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import static com.barchart.util.value.impl.ValueBuilder.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import temp.PriceValue;

import com.barchart.util.bench.size.JavaSize;

public class TestValueBuilder {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void TestNewPrice1() {

		final PriceValue p0 = new DefPrice0(1000);

		final PriceValue p1 = new DefPriceA(1000, 2);

		final PriceValue p2 = new DefPriceB(1000, 2);

		assertEquals(p1.compareTo(p2), 0);
		assertEquals(p2.compareTo(p1), 0);

		assertEquals(JavaSize.of(ValueConst.NULL_PRICE), 8);
		assertEquals(JavaSize.of(p0), 16);
		assertEquals(JavaSize.of(p1), 16);
		assertEquals(JavaSize.of(p2), 24);

		assertTrue(newPrice(0, 0) instanceof NulPrice);

		assertTrue(newPrice(12, 0) instanceof DefPrice0);
		assertTrue(newPrice(122342341, 0) instanceof DefPrice0);
		assertTrue(newPrice(121212121212L, 0) instanceof DefPrice0);

		assertTrue(newPrice(12, 1) instanceof DefPriceA);
		assertTrue(newPrice(122342341, -123) instanceof DefPriceA);

		assertTrue(newPrice(121212121212L, -112) instanceof DefPriceB);

	}

	@Test
	public void TestNewPrice2() {

		assertTrue(newPrice(10, 00) instanceof DefPrice0);
		assertTrue(newPrice(10, -1) instanceof DefPrice1);
		assertTrue(newPrice(10, -2) instanceof DefPrice2);
		assertTrue(newPrice(10, -3) instanceof DefPrice3);
		assertTrue(newPrice(10, -4) instanceof DefPrice4);
		assertTrue(newPrice(10, -5) instanceof DefPrice5);
		assertTrue(newPrice(10, -6) instanceof DefPrice6);
		assertTrue(newPrice(10, -7) instanceof DefPrice7);
		assertTrue(newPrice(10, -8) instanceof DefPrice8);
		assertTrue(newPrice(10, -9) instanceof DefPrice9);

	}

	@Test
	public void TestNewPrice3() {

		// TODO Test from double builder

	}

}
