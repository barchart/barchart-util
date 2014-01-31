package com.barchart.util.value.impl;

import static java.lang.Math.pow;

import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;

public class DefFraction extends BaseDecimal implements Fraction {

	final int base;
	final int exponent;
	final long numerator;
	final long denominator;

	final int decimalExponent;
	final long decimalDenominator;

	final int places;

	public DefFraction(final int base, final int exponent) {

		this.base = base;
		this.exponent = exponent;

		numerator = 1l;
		denominator = (long) pow(base, -exponent);

		decimalExponent = exponent;
		decimalDenominator = (long) pow(10, -decimalExponent);

		places = 1 + (int) Math.log10(denominator);

	}

	@Override
	public long mantissa() {
		return 0;
	}

	@Override
	public long base() {
		return base;
	}

	@Override
	public int exponent() {
		return exponent;
	}

	@Override
	public long numerator() {
		return numerator;
	}

	@Override
	public long denominator() {
		return denominator;
	}

	@Override
	public int decimalExponent() {
		return exponent;
	}

	@Override
	public long decimalDenominator() {
		return decimalDenominator;
	}

	@Override
	public int places() {
		return places;
	}

	@Override
	public boolean isSmallerThan(final Fraction that) {
		if (that == null) {
			return false;
		}
		return decimalExponent < that.decimalExponent();
	}

	@Override
	public long priceFraction(final Price price) {
		return priceFraction(price.mantissa(), price.exponent());
	}

	@Override
	public long priceFraction(long mantissa, int exponent) {

		while (exponent > decimalExponent) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < decimalExponent) {
			mantissa /= 10;
			exponent++;
		}

		mantissa = Math.abs(mantissa);
		mantissa %= decimalDenominator;
		mantissa *= denominator;
		mantissa /= decimalDenominator;

		return mantissa;
	}

	@Override
	public long priceWhole(Price price) {

		if (price == null) {
			price = Price.NULL;
		}
		return priceWhole(price.mantissa(), price.exponent());
	}

	@Override
	public long priceWhole(long mantissa, int exponent) {

		while (exponent > decimalExponent) {
			mantissa *= 10;
			exponent--;
		}

		while (exponent < decimalExponent) {
			mantissa /= 10;
			exponent++;
		}

		return mantissa / decimalDenominator;
	}

}
