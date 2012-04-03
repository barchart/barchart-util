/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.values.provider.DefTextString;

public class TestDefTextString {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testToUpperCase() {

		assertEquals(new DefTextString("").toUpperCase(), "");

		assertEquals(new DefTextString("x").toUpperCase(), "X");

		assertEquals(new DefTextString("aB1c3D-Az23").toUpperCase(),
				"AB1C3D-AZ23");

	}

	@Test
	public void testToLowerCase() {

		assertEquals(new DefTextString("").toLowerCase(), "");

		assertEquals(new DefTextString("x").toLowerCase(), "x");

		assertEquals(new DefTextString("X").toLowerCase(), "x");

		assertEquals(new DefTextString("aB1c3D-Az23").toLowerCase(),
				"ab1c3d-az23");

	}

}
