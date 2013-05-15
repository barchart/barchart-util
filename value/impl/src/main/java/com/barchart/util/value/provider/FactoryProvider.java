package com.barchart.util.value.provider;

import java.util.ServiceLoader;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

/**
 * Value factory provider.
 * <p>
 * TODO implement {@link ServiceLoader}. Will work for OSGI, Guice, plain JVM.
 */
public class FactoryProvider implements Factory {

	static FactoryProvider provider = new FactoryProvider();

	public static Factory instance() {
		return provider;
	}

	@Override
	public Decimal newDecimal(final long mantissa, final int exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Price newPrice(final long mantissa, final int exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Size newSize(final long mantissa, final int exponent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time newTime(final long millisecond, final String zone) {
		// TODO Auto-generated method stub
		return null;
	}

}
