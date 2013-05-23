/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;

import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.values.api.SizeValue;

//16 bytes on 32 bit JVM
@Mutable
@NotThreadSafe
final class VarSize extends BaseSize {

	volatile long mantissa;
	volatile int exponent;

	VarSize(final long mantissa, final int exponent) {
		this.mantissa = mantissa;
		this.exponent = exponent;
	}
	
	VarSize(final long mantissa) {
		this.mantissa = mantissa;
		exponent = 0;
	}

	@Override
	public final long asLong() {
		return (long) (mantissa * 10 ^ exponent); //TODO Not sure if this is right
	}

	@Override
	protected final SizeValue returnSize(final long value) {
		this.mantissa = value;
		return this;
	}

	@Override
	public final SizeValue freeze() {
		return newSize(mantissa, exponent);
	}

	@Override
	public final boolean isFrozen() {
		return false;
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
