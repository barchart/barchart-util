package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

/**
 * Value factory.
 * <p>
 * Use {@link FactoryLoader} to load instance from provider.
 */
@ProviderType
public interface Factory {

	Decimal newDecimal(long mantissa, int exponent);

	Price newPrice(long mantissa, int exponent);

	Size newSize(long mantissa, int exponent);

	Time newTime(long millisecond, String zone);

	TimeInterval newTimeInterval(long beginMill, long endMill);

	Schedule newSchedule(TimeInterval[] intervals);

}
