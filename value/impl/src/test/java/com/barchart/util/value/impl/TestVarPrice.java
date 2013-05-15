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

public class TestVarPrice {

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
		final PriceValue p1 = newPriceMutable(1000, 2);
		final PriceValue p2 = newPriceMutable(1000, 2);
		assertEquals(p1.compareTo(p2), 0);
	}

	@Test
	public void TestPriceMutable1() {

		final PriceValue p1 = newPriceMutable(1000, 2);
		final PriceValue p2 = newPriceMutable(1000, 2);

		//

		assertTrue(p1 instanceof VarPrice);
		assertTrue(p2 instanceof VarPrice);

		//
		assertEquals(p1.compareTo(p2), 0);
		assertEquals(p2.compareTo(p1), 0);
		assertEquals(p1, p2);
		assertTrue(p1 != p2);

		//

		final PriceValue p3 = p1.add(p2);
		assertTrue(p3 instanceof VarPrice);
		assertTrue(p3 == p1);
		assertTrue(p2 != p1);
		assertEquals(p1.mantissa(), 2000);

		//

		p1.mult(2);
		assertEquals(p1.mantissa(), 4000);

		//

		p1.div(4);
		assertEquals(p1, p2);
		assertTrue(p1.equals(p2));
		assertEquals(p1.count(p1), 1);

		//

	}

}
