package com.barchart.util.value.provider;

import java.util.ServiceLoader;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.impl.ValueBuilder;

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

}
