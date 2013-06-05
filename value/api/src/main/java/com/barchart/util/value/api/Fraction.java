package com.barchart.util.value.api;


public interface Fraction extends Decimal {

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

}
