/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import temp.DecimalValue;
import temp.Fraction;
import temp.PriceValue;
import temp.SizeValue;
import temp.TimeInterval;
import temp.TimeValue;

import com.barchart.util.math.DoubleParts;
import com.barchart.util.math.MathExtra;
import com.barchart.util.math.MathIEEE754;

public final class ValueBuilder {

	private ValueBuilder() {
	}

	public static final PriceValue newPrice(final double price) {
		final DoubleParts part = MathIEEE754.extractDecimal(price);
		return ValueBuilder.newPrice(part.getMantissa(), part.getExponent());
	}

	public static final PriceValue newPrice(final long mantissa) {
		if (mantissa == 0) {
			return ValueConst.ZERO_PRICE;
		} else {
			return new DefPrice0(mantissa);
		}
	}

	public static final PriceValue newPrice(final long mantissa,
			final int exponent) throws ArithmeticException {
		switch (exponent) {
		case -9:
			return new DefPrice9(mantissa);
		case -8:
			return new DefPrice8(mantissa);
		case -7:
			return new DefPrice7(mantissa);
		case -6:
			return new DefPrice6(mantissa);
		case -5:
			return new DefPrice5(mantissa);
		case -4:
			return new DefPrice4(mantissa);
		case -3:
			return new DefPrice3(mantissa);
		case -2:
			return new DefPrice2(mantissa);
		case -1:
			return new DefPrice1(mantissa);
		case 00:
			return newPrice(mantissa);
		default:
			MathExtra.castIntToByte(exponent);
			final int mantSmall = (int) mantissa;
			if (mantSmall == mantissa) {
				return new DefPriceA(mantSmall, exponent);
			} else {
				return new DefPriceB(mantissa, exponent);
			}
		}
	}

	public static final PriceValue newPriceMutable(final long mantissa,
			final int exponent) throws ArithmeticException {

		MathExtra.castIntToByte(exponent);

		return new VarPrice(mantissa, exponent);

	}

	private static final int SIZE_CACHE_LIMIT = 1024;

	private static final SizeValue[] SIZE_CACHE = new SizeValue[SIZE_CACHE_LIMIT];

	static {
		for (int k = 0; k < SIZE_CACHE_LIMIT; k++) {
			SIZE_CACHE[k] = new DefSize(k);
		}
		SIZE_CACHE[0] = ValueConst.ZERO_SIZE;
	}

	public static final SizeValue newSize(final long size) {
		if (0 <= size && size < SIZE_CACHE_LIMIT) {
			return SIZE_CACHE[(int) size];
		} else {
			return new DefSize(size);
		}
	}

	public static final SizeValue newSizeMutable(final long size) {
		return new VarSize(size);
	}

	public static final TimeValue newTime(final long time) {
		return new DefTime(time);
	}

	public static final TimeValue newTimeMutable(final long time) {
		return new VarTime(time);
	}

	private static final boolean hasZeroBytes(final byte[] array) {
		for (final byte b : array) {
			if (b == 0) {
				return true;
			}
		}
		return false;
	}

	public static final boolean isStrictMultiple(final PriceValue priceTest,
			final PriceValue priceStep) {
		final long count = priceTest.count(priceStep);
		final PriceValue priceBack = priceStep.mult(count);
		if (priceBack.equals(priceTest)) {
			return true;
		} else {
			return false;
		}
	}

	// do not use - not thread safe
	// public static final boolean isPureAscii(final String string) {
	// return ASCII.ASCII_ENCODER.canEncode(string);
	// }

	public static final boolean isPureAscii(final CharSequence seq) {
		final int size = seq.length();
		for (int k = 0; k < size; k++) {
			if ((seq.charAt(k) & 0xFF00) != 0) {
				return false;
			}
		}
		return true;
	}

	public static DecimalValue newDecimal(final long mantissa,
			final int exponent) {
		return new DefDecimal(mantissa, exponent);
	}

	public static DecimalValue newDecimalMutable(final long mantissa,
			final int exponent) {
		return new VarDecimal(mantissa, exponent);
	}

	public static TimeInterval newTimeInterval(final TimeValue start,
			final TimeValue stop) {
		return new DefTimeInterval(start, stop);
	}

	public static TimeInterval newTimeInterval(final long start, final long stop) {
		return new DefTimeInterval(newTime(start), newTime(stop));
	}

	public static Fraction newFraction(final int base, final int exponent) {
		return new DefFraction(base, exponent);
	}

}
