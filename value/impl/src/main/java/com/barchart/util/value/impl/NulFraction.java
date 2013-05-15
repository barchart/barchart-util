package com.barchart.util.value.impl;

import temp.Fraction;
import temp.PriceValue;

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
	public boolean isSmallerThan(final Fraction that) {
		return false;
	}

	@Override
	public long priceFraction(final PriceValue price) {
		return 0;
	}

	@Override
	public long priceFraction(final long mantissa, final int exponent) {
		return 0;
	}

	@Override
	public long priceWhole(final long mantissa, final int exponent) {
		return 0;
	}

	@Override
	public long priceWhole(final PriceValue price) {
		return 0;
	}

}
