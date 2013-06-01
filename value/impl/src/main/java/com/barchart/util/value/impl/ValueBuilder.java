/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.math.DoubleParts;
import com.barchart.util.math.MathExtra;
import com.barchart.util.math.MathIEEE754;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

/**
 * NOTE: this class is bundle-private in OSGI.
 * <p>
 * FIXME review optimizations.
 */
public final class ValueBuilder {

	private ValueBuilder() {
	}

	public static final Price newPrice(final double price) {
		final DoubleParts part = MathIEEE754.extractDecimal(price);
		return ValueBuilder.newPrice(part.getMantissa(), part.getExponent());
	}

	public static final Price newPrice(final long mantissa) {
		if (mantissa == 0) {
			return ValueConst.ZERO_PRICE;
		} else {
			return new DefPrice0(mantissa);
		}
	}

	public static final Price newPrice(final long mantissa, final int exponent)
			throws ArithmeticException {
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

	public static final Price newPriceMutable(final long mantissa,
			final int exponent) throws ArithmeticException {

		MathExtra.castIntToByte(exponent);

		return new VarPrice(mantissa, exponent);

	}

	private static final int SIZE_CACHE_LIMIT = 1024;

	private static final Size[] SIZE_CACHE = new Size[SIZE_CACHE_LIMIT];

	static {
		for (int k = 0; k < SIZE_CACHE_LIMIT; k++) {
			SIZE_CACHE[k] = new DefSize(k, 0);
		}
		SIZE_CACHE[0] = ValueConst.ZERO_SIZE;
	}

	public static final Size newSize(final long size) {
		if (0 <= size && size < SIZE_CACHE_LIMIT) {
			return SIZE_CACHE[(int) size];
		} else {
			return new DefSize(size, 0);
		}
	}

	public static final Size newSize(final long mantissa, final int exponent) {
		return new DefSize(mantissa, exponent);
	}

	public static final Size newSizeMutable(final long mantissa,
			final int exponent) {
		return new VarSize(mantissa, exponent);
	}

	public static final Time newTime(final long time) {
		return new DefTime(time);
	}

	public static final Time newTimeMutable(final long time) {
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

	public static final boolean isStrictMultiple(final Price priceTest,
			final Price priceStep) {
		final long count = priceTest.count(priceStep);
		final Price priceBack = priceStep.mult(count);
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

	public static Decimal newDecimal(final long mantissa, final int exponent) {
		return new DefDecimal(mantissa, exponent);
	}

	public static Decimal newDecimalMutable(final long mantissa,
			final int exponent) {
		return new VarDecimal(mantissa, exponent);
	}

	public static TimeInterval newTimeInterval(final Time start, final Time stop) {
		return new DefTimeInterval(start, stop);
	}

	public static TimeInterval newTimeInterval(final long start, final long stop) {
		return new DefTimeInterval(newTime(start), newTime(stop));
	}

	public static Schedule newSchedule(final TimeInterval[] intervals) {
		return new BaseSchedule(intervals);
	}

}
