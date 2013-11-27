/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.common.math;

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