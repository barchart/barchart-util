package com.barchart.util.math;

public class FloatParts {

	private final int mantissa;
	private final int exponent;

	public long getMantissa() {
		return mantissa;
	}

	public int getExponent() {
		return exponent;
	}

	public FloatParts(final int mantissa, final int exponent) {
		this.mantissa = mantissa;
		this.exponent = exponent;
	}

}