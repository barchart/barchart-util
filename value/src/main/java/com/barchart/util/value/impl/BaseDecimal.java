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

@NotMutable
abstract class BaseDecimal extends BaseScaled<Decimal> implements
		Decimal {

	@Override
	protected Decimal result(final long mantissa, final int exponent) {
		return newDecimal(mantissa, exponent);
	}

	@Override
	public final boolean equals(final Object thatValue) {
		if (thatValue instanceof Decimal) {
			final Decimal that = (Decimal) thatValue;
			return this.compareTo(that) == 0;
		}
		return false;
	}
	
	@Override
	public boolean isNull() {
		return this == Decimal.NULL;
	}

}
