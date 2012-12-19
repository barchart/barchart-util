/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.tostring;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.provider.ValueBuilder;

public class TestDecimalValueToString {

	@Test
	public void test0() {
		assertEquals("0", value(0, 0));
	}

	@Test
	public void test1() {
		assertEquals("1", value(1, 0));
	}

	@Test
	public void test10() {
		assertEquals("10", value(1, 1));
	}

	@Test
	public void test100() {
		assertEquals("100", value(1, 2));
	}

	@Test
	public void test1000() {
		assertEquals("1000", value(1, 3));
	}
	
	@Test
	public void test1234000() {
		assertEquals("1234000", value(1234, 3));
	}
	
	@Test
	public void testDecimal1() {
		assertEquals("0.25", value(25, -2));
	}
	
	@Test
	public void testDecimal2() {
		assertEquals("0.0005", value(5, -4));
	}
	
	@Test
	public void testDecimal3() {
		assertEquals("0.000078", value(78, -6));
	}
	

	

	


	

	private String value(long mantissa, int exponent) {
		DecimalValue value = ValueBuilder.newDecimal(mantissa, exponent);
		return value.toString();
	}
}
