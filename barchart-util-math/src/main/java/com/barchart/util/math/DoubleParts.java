/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
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
