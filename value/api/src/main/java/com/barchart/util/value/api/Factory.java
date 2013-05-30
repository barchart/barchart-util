package com.barchart.util.value.api;

import java.util.ServiceLoader;

import aQute.bnd.annotation.ProviderType;

/**
 * Value factory.
 * <p>
 * TODO implement {@link ServiceLoader}. Will work for OSGI, Guice, plain JVM.
 */
@ProviderType
public interface Factory {

	Factory INSTANCE = null; // TODO ServiceLoader

	Decimal newDecimal(long mantissa, int exponent);

	Price newPrice(long mantissa, int exponent);

	Size newSize(long mantissa, int exponent);

	Time newTime(long millisecond, String zone);
	
	TimeInterval newTimeInterval(long beginMill, long endMill);
	
	Schedule newSchedule(TimeInterval[] intervals);

}
