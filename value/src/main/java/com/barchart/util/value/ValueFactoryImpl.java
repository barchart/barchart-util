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

	public ValueFactoryImpl() {
		super();
	}

	/* ***** ***** Decimals ***** ***** */

	@Override
	public Decimal newDecimal(long mantissa, int exponent) {
		return decimal(mantissa, exponent);
	}
	
	@Override
	public Decimal newDecimal(final double dec) {
		return decimal(dec);
	}

	/* ***** ***** Prices ***** ***** */

	@Override
	public Price newPrice(long mantissa, int exponent) {
		return price(mantissa, exponent);
	}

	@Override
	public Price newPrice(double price) {
		return price(price);
	}

	/* ***** ***** Sizes ***** ***** */

	@Override
	public Size newSize(long mantissa, int exponent) {
		return size(mantissa, exponent);
	}

	@Override
	public Size newSize(long size) {
		return size(size);
	}

	/* ***** ***** Fractions ***** ***** */

	@Override
	public Fraction newFraction(int base, int exponent) {
		return fraction(base, exponent);
	}

	/* ***** ***** Times ***** ***** */

	@Override
	public Time newTime(long millisecond, TimeZone zone) {
		return time(millisecond, zone);
	}

	@Override
	public Time newTime(long millisecond, String zone) {
		return time(millisecond, zone);
	}

	@Override
	public Time newTime(long millisecond) {
		return time(millisecond);
	}

	@Override
	public Time newTime(Date date) {
		return time(date.getTime());
	}

	@Override
	public Time newTime(Date date, TimeZone zone) {
		return time(date.getTime(), zone);
	}

	@Override
	public Time newTime(Date date, String zone) {
		return time(date.getTime(), zone);
	}

	/* ***** ***** Time intervals ***** ***** */

	@Override
	public TimeInterval newTimeInterval(long beginMill, long endMill) {
		return timeInterval(beginMill, endMill);
	}

	/* ***** ***** Schedule ***** ***** */

	@Override
	public Schedule newSchedule(TimeInterval[] intervals) {
		return schedule(intervals);
	}

	@Override
	public Bool newBoolean(boolean value) {
		return bool(value);
	}
	
}
