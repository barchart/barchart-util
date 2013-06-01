package com.barchart.util.value.provider;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.impl.ValueBuilder;

/**
 * Value factory provider.
 */
public class FactoryProvider implements Factory {

	static final Factory INSTANCE = new FactoryProvider();

	/**
	 * Verify semantic version match outside OSGI.
	 */
	static void assertVersion() throws Exception {

	}

	public static Factory instance() throws Exception {
		assertVersion();
		return INSTANCE;
	}

	@Override
	public Decimal newDecimal(final long mantissa, final int exponent) {
		return ValueBuilder.newDecimal(mantissa, exponent);
	}

	@Override
	public Price newPrice(final long mantissa, final int exponent) {
		return ValueBuilder.newPrice(mantissa, exponent);
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

	@Override
	public Schedule newSchedule(final TimeInterval[] intervals) {
		return ValueBuilder.newSchedule(intervals);
	}

}
