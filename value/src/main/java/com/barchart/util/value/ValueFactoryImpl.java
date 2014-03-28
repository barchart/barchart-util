package com.barchart.util.value;

import java.util.Date;
import java.util.TimeZone;

import com.barchart.util.value.api.Bool;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;
import com.barchart.util.value.impl.ValueBuilder;

/**
 * Value factory implementation.
 */
public class ValueFactoryImpl extends ValueBuilder implements ValueFactory {

	public static final ValueFactory instance = new ValueFactoryImpl();

	public ValueFactoryImpl() {
		super();
	}

	public static ValueFactory getInstance() {
		return instance;
	}

	/* ***** ***** Decimals ***** ***** */

	@Override
	public Decimal newDecimal(final long mantissa, final int exponent) {
		return decimal(mantissa, exponent);
	}

	@Override
	public Decimal newDecimal(final double dec) {
		return decimal(dec);
	}

	/* ***** ***** Prices ***** ***** */

	@Override
	public Price newPrice(final long mantissa, final int exponent) {
		return price(mantissa, exponent);
	}

	@Override
	public Price newPrice(final double price) {
		return price(price);
	}

	/* ***** ***** Sizes ***** ***** */

	@Override
	public Size newSize(final long mantissa, final int exponent) {
		return size(mantissa, exponent);
	}

	@Override
	public Size newSize(final long size) {
		return size(size);
	}

	/* ***** ***** Fractions ***** ***** */

	@Override
	public Fraction newFraction(final int base, final int exponent) {
		return fraction(base, exponent);
	}

	/* ***** ***** Times ***** ***** */

	@Override
	public Time newTime(final long millisecond, final TimeZone zone) {
		return time(millisecond, zone);
	}

	@Override
	public Time newTime(final long millisecond, final String zone) {
		return time(millisecond, zone);
	}

	@Override
	public Time newTime(final long millisecond) {
		return time(millisecond);
	}

	@Override
	public Time newTime(final Date date) {
		return time(date.getTime());
	}

	@Override
	public Time newTime(final Date date, final TimeZone zone) {
		return time(date.getTime(), zone);
	}

	@Override
	public Time newTime(final Date date, final String zone) {
		return time(date.getTime(), zone);
	}

	/* ***** ***** Time intervals ***** ***** */

	@Override
	public TimeInterval newTimeInterval(final long beginMill, final long endMill) {
		return timeInterval(beginMill, endMill);
	}

	/* ***** ***** Schedule ***** ***** */

	@Override
	public Schedule newSchedule(final TimeInterval[] intervals) {
		return schedule(intervals);
	}

	@Override
	public Bool newBoolean(final boolean value) {
		return bool(value);
	}

}
