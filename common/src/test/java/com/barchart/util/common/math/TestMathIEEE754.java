/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.common.math;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

public class TestMathIEEE754 {

	@Test
	public void testZero() {
		
		final double zero = 0.0;
		final DoubleParts parts = MathIEEE754.extractDecimal(zero);
		
		assertEquals(parts.getMantissa(), 0l);
		assertEquals(parts.getExponent(), 0);
		
	}
	
	@Test
	public void testMantissa0() {

		final long m1 = 12345;
		final int e1 = -6;

		final double v1 = m1 * Math.pow(10, e1);

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(m1, m2);
		assertEquals(e1, e2);

	}

	@Test
	public void testMantissa1() {

		final long m1 = -12345;
		final int e1 = +6;

		final double v1 = m1 * Math.pow(10, e1);

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(m1, m2);
		assertEquals(e1, e2);

	}

	@Test
	public void testMantissa3() {

		final long m1 = 1234567;
		final int e1 = -3;

		final double v1 = m1 * Math.pow(10, e1);

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(m1, m2);
		assertEquals(e1, e2);

	}

	@Test
	public void testMantissa4() {

		final double v1 = -1234.5678;

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(-12345678, m2);
		assertEquals(-4, e2);

	}

	@Test
	public void testMantissa5() {

		final double v1 = 12345.56789;

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(1234556789, m2);
		assertEquals(-5, e2);

	}

	@Test
	public void testMantissa6() {

		final double v1 = -123456.456789;

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(-123456456789L, m2);
		assertEquals(-6, e2);

	}

	@Test
	public void testMantissa7() {

		final double v1 = 123456.3456789;

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(1234563456789L, m2);
		assertEquals(-7, e2);

	}

	@Test
	public void testMantissa8() {

		final double v1 = -1.234563456789;

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(-1234563456789L, m2);
		assertEquals(-12, e2);

	}

	@Test
	public void testMantissa9() {

		final double v1 = 315.234563456789;

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();

		assertEquals(315234563456789L, m2);
		assertEquals(-12, e2);

	}

	@Test
	public void testMantissa10() {

		final double v1 = -1681.235123;

		// System.out.println("v1=" + v1);

		final DoubleParts parts = MathIEEE754.extractDecimal(v1);

		final long m2 = parts.getMantissa();
		final long e2 = parts.getExponent();
		// System.out.println("m2=" + m2);
		// System.out.println("e2=" + e2);

		assertEquals(-1681235123L, m2);
		assertEquals(-6, e2);

	}

	static class DecimalParts {

		double value;
		long mantissa;
		int exponent;

		@Override
		public String toString() {
			return value + ":" + mantissa + "/" + exponent;
		}

	}

	static DecimalParts makeDecimal(final Random random, final long wholeRange,
			final long fractRange) {

		final long sign = random.nextInt(10) - 5;

		final long whole = sign * (long) (random.nextDouble() * wholeRange);
		long fract = (long) (random.nextDouble() * fractRange);

		while (fract % 10 == 0) {
			fract += 1;
		}

		final String textWhole = Long.toString(whole);
		final String textFract = Long.toString(fract);

		final String textDouble = textWhole + "." + fract;

		final DecimalParts triple = new DecimalParts();
		triple.value = Double.parseDouble(textDouble);
		triple.mantissa = Long.parseLong(textWhole + textFract);
		triple.exponent = -textFract.length();

		return triple;

	}

	static void testExtract(final long wholeRange, final long fractRange,
			final int count) {

		final Random random = new Random(0);

		for (int index = 0; index < count; index++) {

			// System.out.println("index=" + index);

			final DecimalParts decimal = makeDecimal(random, wholeRange,
					fractRange);

			// System.out.println("decimal=" + decimal);

			final DoubleParts parts = MathIEEE754.extractDecimal(decimal.value);

			assertEquals(decimal.mantissa, parts.getMantissa());
			assertEquals(decimal.exponent, parts.getExponent());

		}

	}

	static final int COUNT = 100000;

	@Test
	public void testRandom1() {
		testExtract(10, 10, COUNT);
		testExtract(100, 1, COUNT);
	}

	@Test
	public void testRandom2() {
		testExtract(100, 100, COUNT);
		testExtract(1000, 10, COUNT);
	}

	@Test
	public void testRandom3() {
		testExtract(1000, 1000, COUNT);
		testExtract(100000, 10, COUNT);
	}

	@Test
	public void testRandom4() {
		testExtract(10000, 10000, COUNT);
		testExtract(10000000, 10, COUNT);
	}

	@Test
	public void testRandom5() {
		testExtract(100000, 100000, COUNT);
		testExtract(1000000000, 10, COUNT);
	}

	@Test
	public void testRandom6() {
		testExtract(1000000, 1000000, COUNT);
		testExtract(1000000000000L, 10, COUNT);
	}

	@Test
	public void testRandom7() {
		testExtract(10000000, 10000000, COUNT);
		testExtract(10000000000000L, 1, COUNT);
	}

}
