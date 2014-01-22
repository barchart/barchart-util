package com.barchart.util.value;

import com.barchart.util.value.api.Bool;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;
import com.barchart.util.value.impl.DefFraction;
import com.barchart.util.value.impl.ValueBuilder;

/**
 * Value factory implementation.
 */
public class ValueFactoryImpl implements ValueFactory {
	
	public static final ValueFactory factory = new ValueFactoryImpl();

	@Override
	public Decimal newDecimal(final long mantissa, final int exponent) {
		return ValueBuilder.newDecimal(mantissa, exponent);
	}

	@Override
	public Fraction newFraction(final int base, final int exponent) {
		return new DefFraction(base, exponent);
	}

	@Override
	public Price newPrice(final long mantissa, final int exponent) {
		return ValueBuilder.newPrice(mantissa, exponent);
	}

	@Override
	public Price newPrice(double price) {
		return ValueBuilder.newPrice(price);
	}
	
	@Override
	public Schedule newSchedule(final TimeInterval[] intervals) {
		return ValueBuilder.newSchedule(intervals);
	}

	@Override
	public Size newSize(final long mantissa, final int exponent) {
		return ValueBuilder.newSize(mantissa, exponent);
	}

	@Override
	public Time newTime(final long millisecond, final String zone) {
		return ValueBuilder.newTime(millisecond);
	}
	
	@Override
	public Time newTime(long millisecond) {
		return newTime(millisecond, "UTC");
	}

	@Override
	public TimeInterval newTimeInterval(final long beginMill, final long endMill) {
		return ValueBuilder.newTimeInterval(ValueBuilder.newTime(beginMill),
				ValueBuilder.newTime(endMill));
	}

	@Override
	public Bool newBoolean(boolean value) {
		return ValueBuilder.newBoolean(value);
	}

}
