/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import static com.barchart.util.value.impl.ValueBuilder.*;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Decimal;

// 8 bytes on 32 bit JVM
@NotMutable
final class NulDecimal extends BaseDecimal {

	@Override
	public long mantissa() {
		return 0;
	}

	@Override
	public int exponent() {
		return 0;
	}

	@Override
	protected Decimal result(final long mantissa, final int exponent) {
		return newDecimal(mantissa, exponent);
	}

}
