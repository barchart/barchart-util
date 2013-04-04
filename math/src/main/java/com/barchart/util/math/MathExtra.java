/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.math;

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

}
