/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import java.util.TimeZone;

import com.barchart.util.value.api.Bool;
import com.barchart.util.value.api.Day;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.LocalTime;
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
public class ValueBuilder {

	private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

	public ValueBuilder() {
	}

	protected final Price price(final double price) {
		final DoubleParts part = MathIEEE754.extractDecimal(price);
		return price(part.getMantissa(), part.getExponent());
	}

	protected final Price price(final long mantissa, final int exponent)
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
				return new DefPrice0(mantissa);
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

	private static final int SIZE_CACHE_LIMIT = 1024;

	private static final Size[] SIZE_CACHE = new Size[SIZE_CACHE_LIMIT];

	static {
		for (int k = 0; k < SIZE_CACHE_LIMIT; k++) {
			SIZE_CACHE[k] = new DefSize(k, 0);
		}
		SIZE_CACHE[0] = Size.ZERO;
	}

	protected final Size size(final long size) {
		if (0 <= size && size < SIZE_CACHE_LIMIT) {
			return SIZE_CACHE[(int) size];
		} else {
			return new DefSize(size, 0);
		}
	}

	protected final Size size(final long mantissa, final int exponent) {
		return new DefSize(mantissa, exponent);
	}

	protected final Time time(final long time) {
		return new TimeImpl(time, UTC);
	}

	// public static final boolean isStrictMultiple(final Price priceTest,
	// final Price priceStep) {
	// final long count = priceTest.count(priceStep);
	// final Price priceBack = priceStep.mult(count);
	// if (priceBack.equals(priceTest)) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	// do not use - not thread safe
	// public static final boolean isPureAscii(final String string) {
	// return ASCII.ASCII_ENCODER.canEncode(string);
	// }
	// public static final boolean isPureAscii(final CharSequence seq) {
	// final int size = seq.length();
	// for (int k = 0; k < size; k++) {
	// if ((seq.charAt(k) & 0xFF00) != 0) {
	// return false;
	// }
	// }
	// return true;
	// }

	protected Decimal decimal(final long mantissa, final int exponent) {
		return new DefDecimal(mantissa, exponent);
	}

	protected TimeInterval timeInterval(final Time start, final Time stop) {
		return new DefTimeInterval(start, stop);
	}

	protected TimeInterval timeInterval(final long start, final long stop) {
		return new DefTimeInterval(time(start), time(stop));
	}

	protected Schedule schedule(final TimeInterval[] intervals) {
		return new BaseSchedule(intervals);
	}

	protected Bool bool(final boolean value) {
		return new DefBool(value);
	}

	protected Fraction fraction(int base, int exponent) {
		return new DefFraction(base, exponent);
	}

	protected Time time(long millisecond, String zone) {
		return new TimeImpl(millisecond, TimeZone.getTimeZone(zone));
	}

	protected Time time(long millisecond, final TimeZone zone) {
		return new TimeImpl(millisecond, zone);
	}

	protected LocalTime localTime(long millisecond) {
		return new LocalTimeImpl(millisecond);
	}

	protected Day day(long millisecond) {
		return new DayImpl(millisecond);
	}

}
