/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.SizeValue;

//16 bytes on 32 bit JVM
@NotMutable
final class DefSize extends BaseSizeFrozen {

	final long mantissa;
	final int exponent;

	DefSize(final long mantissa, final int exponent) {
		this.mantissa = mantissa;
		this.exponent = exponent;
	}
	
	DefSize(final long mantissa) {
		this.mantissa = mantissa;
		exponent = 0;
	}

	@Override
	public final long asLong() {
		return (long) (mantissa * 10 ^ exponent); //TODO Not sure if this is right
	}

	@Override
	protected final SizeValue returnSize(long value) {
		return ValueBuilder.newSize(value);
	}

	@Override
	public long mantissa() {
		return mantissa;
	}
	
	@Override
	public int exponent() {
		return exponent;
	}
	

}