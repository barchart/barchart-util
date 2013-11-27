/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.common.math;

/**
 * http://en.wikipedia.org/wiki/IEEE_754-2008
 */
public class MathIEEE754 {

	private static final long BASIS = 0L;
	private static final long LIMIT = 10000L;
	private static final long BELOW = LIMIT / 10L;
	private static final long ABOVE = LIMIT - LIMIT / 10L;

	public final static boolean isFinite(final double d) {
		return Math.getExponent(d) <= Double.MAX_EXPONENT;
	}

	public final static boolean isNormal(final double d) {
		return Math.getExponent(d) >= Double.MIN_EXPONENT;
	}

	/**
	 * extracts floating point parts from a double value in binary
	 * representation;
	 * 
	 * @return floating point parts in binary representation;
	 * 
	 */
	public static final DoubleParts extractBinary(final double value) {

		/**
		 * Translate the double into sign, exponent and mantissa, according to
		 * the formulae in JLS, Section 20.10.22.
		 */

		final long bits = Double.doubleToLongBits(value);

		final int sign = ((bits >> 63) == 0 ? 1 : -1);

		int exponentBinary = (int) ((bits >> 52) & 0x7ffL);

		long mantissaBinary = (exponentBinary == 0 ? (bits & ((1L << 52) - 1)) << 1
				: (bits & ((1L << 52) - 1)) | (1L << 52));

		exponentBinary -= 1075;

		/** value == sign * mantissa * 2 ^ exponent */

		mantissaBinary *= sign;

		return new DoubleParts(mantissaBinary, exponentBinary);

	}

	/**
	 * extracts floating point parts from a double value in decimal
	 * representation;
	 * 
	 * approximation; works correct till about 14 decimal digits in mantissa;
	 * 
	 * suitable for double values parsed from string representations like
	 * MMMMMM.EEEEE which does not use "E" exponent notation and are safe for
	 * double round-trip conversions
	 * 
	 * @return floating point parts in decimal representation, normalized;
	 */
	public static final DoubleParts extractDecimal(final double value) {

		/**
		 * Translate the double into sign, exponent and mantissa, according to
		 * the formulae in JLS, Section 20.10.22.
		 */

		if(value == 0.0) {
			return new DoubleParts(0,0);
		}
		
		final long bits = Double.doubleToLongBits(value);

		final int sign = ((bits >> 63) == 0 ? 1 : -1);

		int binaryExponent = (int) ((bits >> 52) & 0x7ffL);

		final long binaryMantissa = (binaryExponent == 0 ? (bits & ((1L << 52) - 1)) << 1
				: (bits & ((1L << 52) - 1)) | (1L << 52));

		binaryExponent -= 1075;

		/** value == sign * mantissa * 2 ^ exponent */

		/** uses in-place mantissa transform */

		long decimalMantissa = binaryMantissa;
		int decimalExponent = 0;

		/** replace binary with decimal */
		while (binaryExponent < 0) {
			if (decimalMantissa < Long.MAX_VALUE / 5) {
				/** divide by 2, multiply by 10 */
				decimalMantissa *= 5;
				decimalExponent -= 1;
				binaryExponent += 1;
			} else {
				/** divide by 2 */
				decimalMantissa >>= 1;
				binaryExponent += 1;
			}
		}

		/** replace binary with decimal */
		while (binaryExponent > 0) {
			if (decimalMantissa < Long.MAX_VALUE / 5) {
				/** multiply by 2 */
				decimalMantissa <<= 1;
				binaryExponent -= 1;
			} else {
				/** multiply by 2, divide by 10 */
				decimalMantissa /= 5;
				decimalExponent += 1;
				binaryExponent -= 1;
			}
		}

		assert binaryExponent == 0;

		/** round about decimal tails like ...00123 or ...99876 */

		final long truncate = (decimalMantissa / LIMIT) * LIMIT;
		final long distance = (decimalMantissa - truncate);

		assert distance >= 0;

		if (BASIS < distance && distance < BELOW) {
			decimalMantissa -= distance;
		}
		if (ABOVE < distance && distance < LIMIT) {
			decimalMantissa += LIMIT - distance;
		}

		/** normalize decimal representation */

		while (decimalMantissa % 10 == 0) {
			decimalMantissa /= 10;
			decimalExponent += 1;
		}

		/**  */

		if (sign < 0) {
			decimalMantissa = -decimalMantissa;
		}

		return new DoubleParts(decimalMantissa, decimalExponent);

	}

}
