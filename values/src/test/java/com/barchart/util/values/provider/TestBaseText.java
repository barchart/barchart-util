/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
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

import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.provider.DefTextByteArray;
import com.barchart.util.values.provider.DefTextLong;
import com.barchart.util.values.provider.DefTextString;
import com.barchart.util.values.provider.ValueBuilder;

public class TestBaseText {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConcat1() {

		TextValue text1 = new DefTextLong("AB");

		TextValue text2 = new DefTextByteArray("01");

		TextValue text3 = new DefTextString("+=");

		TextValue text12 = text1.concat(text2);
		assertTrue(text12.equals("AB01"));
		// System.err.println("text12=" + text12);

		TextValue text123 = text1.concat(text2).concat(text3);
		assertTrue(text123.equals("AB01+="));
		assertTrue(text123 instanceof DefTextLong);

		// System.err.println("text123=" + text123);

	}

	@Test
	public void testConcat2() {

		TextValue text1 = ValueBuilder.newText("AB");
		assertTrue(text1 instanceof DefTextLong);

		TextValue text2 = ValueBuilder.newText("012345678");
		assertTrue(text2 instanceof DefTextByteArray);

		TextValue text3 = ValueBuilder.newText("Андрей愛");
		assertTrue(text3 instanceof DefTextString);

		TextValue text123 = text1.concat(text2).concat(text3);
		assertTrue(text123.equals("AB012345678Андрей愛"));
		assertTrue(text123 instanceof DefTextString);

		System.err.println("text123=" + text123);

	}

}
