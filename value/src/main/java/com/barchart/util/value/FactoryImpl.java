package com.barchart.util.value;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.impl.DefFraction;
import com.barchart.util.value.impl.ValueBuilder;

/**
 * Value factory implementation.
 */
public class FactoryImpl implements Factory {
	
	public static final Factory factory = new FactoryImpl();

	@Override
	public Decimal newDecimal(final long mantissa, final int exponent) {
		return ValueBuilder.newDecimal(mantissa, exponent);
	}

	@Override
	public Fraction newFraction(final int numerator, final int denominator) {
		return new DefFraction(numerator, denominator);
	}

	@Override
	public Price newPrice(final long mantissa, final int exponent) {
		return ValueBuilder.newPrice(mantissa, exponent);
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
	public TimeInterval newTimeInterval(final long beginMill, final long endMill) {
		return ValueBuilder.newTimeInterval(ValueBuilder.newTime(beginMill),
				ValueBuilder.newTime(endMill));
	}

}
