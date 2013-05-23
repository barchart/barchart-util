package com.barchart.util.values.provider;

import com.barchart.util.values.api.Fraction;
import com.barchart.util.values.api.PriceValue;

class NulFraction extends BaseDecimal implements Fraction {

	@Override
	public long mantissa() {
		return 0;
	}

	@Override
	public long base() {
		return 0;
	}

	@Override
	public int exponent() {
		return 0;
	}

	@Override
	public long numerator() {
		return 0;
	}

	@Override
	public long denominator() {
		return 0;
	}

	@Override
	public int decimalExponent() {
		return 0;
	}

	@Override
	public long decimalDenominator() {
		return 0;
	}

	@Override
	public int places() {
		return 0;
	}

	@Override
	public boolean isSmallerThan(Fraction that) {
		return false;
	}

	@Override
	public long priceFraction(PriceValue price) {
		return 0;
	}

	@Override
	public long priceFraction(long mantissa, int exponent) {
		return 0;
	}

	@Override
	public long priceWhole(long mantissa, int exponent) {
		return 0;
	}

	@Override
	public long priceWhole(PriceValue price) {
		return 0;
	}

}
