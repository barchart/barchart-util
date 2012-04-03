package com.barchart.util.math;

public class DoubleParts {

	private final long mantissa;
	private final int exponent;

	public long getMantissa() {
		return mantissa;
	}

	public int getExponent() {
		return exponent;
	}

	public DoubleParts(final long mantissa, final int exponent) {
		this.mantissa = mantissa;
		this.exponent = exponent;
	}

}
