/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.lang;

public final class MathExtra {

	private MathExtra() {

	}

	public static final byte castCharToByte(final char value)
			throws ArithmeticException {
		final byte result = (byte) value;
		if (result == value) {
			return result;
		} else {
			throw new ArithmeticException("can not cast char to byte");
		}
	}

	public static final byte castIntToByte(final int value)
			throws ArithmeticException {
		final byte result = (byte) value;
		if (result == value) {
			return result;
		} else {
			throw new ArithmeticException("can not cast int to byte");
		}
	}

	public static byte castLongToByte(final long value)
			throws ArithmeticException {
		final byte result = (byte) value;
		if (result == value) {
			return result;
		} else {
			throw new ArithmeticException("can not cast long to byte");
		}
	}

	public static final int castLongToInt(final long value)
			throws ArithmeticException {
		final int result = (int) value;
		if (result == value) {
			return result;
		} else {
			throw new ArithmeticException("can not cast long to int");
		}
	}

	public static final long longMult(final long a, final long b)
			throws ArithmeticException {
		final long r = a * b;
		if (a != 0 && r / a != b) {
			throw new ArithmeticException("overflow" + " a=" + a + " b=" + b
					+ " r=" + r);
		}
		return r;
	}

	public static final long longMult10(final long v)
			throws ArithmeticException {
		final long r = v * 10L;
		if (r / 10L != v) {
			throw new ArithmeticException("overflow" + " v=" + v + " r=" + r);
		}
		return r;
	}

	public static final int intAdd(final int a, final int b)
			throws ArithmeticException {
		final int r = a + b;
		if (((a ^ r) & (b ^ r)) < 0) {
			throw new ArithmeticException("overflow");
		}
		return r;
	}

	public static final int intSub(final int a, final int b)
			throws ArithmeticException {
		final int r = a + (-b);
		if (((a ^ r) & ((-b) ^ r)) < 0) {
			throw new ArithmeticException("overflow");
		}
		return r;
	}

	public static final long intMult(final int a, final int b)
			throws ArithmeticException {
		final int r = a * b;
		if (a != 0 && r / a != b) {
			throw new ArithmeticException("overflow" + " a=" + a + " b=" + b
					+ " r=" + r);
		}
		return r;
	}

	public static final int intMult10(final int v) throws ArithmeticException {
		final int r = v * 10;
		if (r / 10 != v) {
			throw new ArithmeticException("overflow" + " v=" + v + " r=" + r);
		}
		return r;
	}

	public static final long longAdd(final long a, final long b)
			throws ArithmeticException {
		final long r = a + b;
		if (((a ^ r) & (b ^ r)) < 0) {
			throw new ArithmeticException("overflow");
		}
		return r;
	}

	public static final long longSub(final long a, final long b)
			throws ArithmeticException {
		final long r = a + (-b);
		if (((a ^ r) & ((-b) ^ r)) < 0) {
			throw new ArithmeticException("overflow");
		}
		return r;
	}

	public final static boolean isFinite(final double d) {
		return Math.getExponent(d) <= Double.MAX_EXPONENT;
	}

	public final static boolean isNormal(final double d) {
		return Math.getExponent(d) >= Double.MIN_EXPONENT;
	}

	public static class FloatParts {

		private final int mantissa;
		private final int exponent;

		public long getMantissa() {
			return mantissa;
		}

		public int getExponent() {
			return exponent;
		}

		public FloatParts(final int mantissa, final int exponent) {
			this.mantissa = mantissa;
			this.exponent = exponent;
		}

	}

	public static class DoubleParts {

		private final long mantissa;
		private final int exponent;

		public long getMantissa() {
			return mantissa;
		}

		public int getExponent() {
			return exponent;
		}

		public DoubleParts(final long mantissa, final int exponent) {
			this.mantissa = mantissa;
			this.exponent = exponent;
		}

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

	private static final long BASIS = 0L;
	private static final long LIMIT = 10000L;
	private static final long BELOW = LIMIT / 10L;
	private static final long ABOVE = LIMIT - LIMIT / 10L;

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
