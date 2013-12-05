package com.barchart.util.value.api;

/**
 * Value factory.
 * <p>
 * Use {@link FactoryLoader} to load instance from provider.
 */
// NOTE: Update implementation META-INF/service when moving this class.
public interface Factory {

	Decimal newDecimal(long mantissa, int exponent);

	Price newPrice(long mantissa, int exponent);

	Size newSize(long mantissa, int exponent);

	Fraction newFraction(int numerator, int denominator);

	Time newTime(long millisecond, String zone);

	TimeInterval newTimeInterval(long beginMill, long endMill);

	Schedule newSchedule(TimeInterval[] intervals);

}
