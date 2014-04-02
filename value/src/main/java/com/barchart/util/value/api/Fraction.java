package com.barchart.util.value.api;

import com.barchart.util.value.ValueFactoryImpl;

public interface Fraction extends Decimal, Existential {
	
	/** Special fraction value @see {isNull} */
	Fraction NULL = new ValueFactoryImpl().newFraction(10, -1);

	long base();

	@Override
	int exponent();

	long numerator();

	long denominator();

	int decimalExponent();

	long decimalDenominator();

	int places();

	boolean isSmallerThan(Fraction that);

	long priceFraction(Price price);

	long priceFraction(long mantissa, int exponent);

	long priceWhole(Price price);

	long priceWhole(long mantissa, int exponent);
	
	@Override
	boolean isNull();

}
