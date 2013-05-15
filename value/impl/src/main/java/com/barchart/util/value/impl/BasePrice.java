/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import static com.barchart.util.value.impl.ValueBuilder.*;
import static com.barchart.util.value.impl.ValueConst.*;
import temp.DecimalValue;
import temp.PriceValue;
import temp.ScaledDecimalValue;

import com.barchart.util.anno.NotMutable;

@NotMutable
abstract class BasePrice extends ScaledDecimalValue<PriceValue, DecimalValue>
		implements PriceValue {

	@Override
	protected PriceValue result(final long mantissa, final int exponent) {
		return newPrice(mantissa, exponent);
	}

	@Override
	public final boolean isNull() {
		return this == NULL_PRICE;
	}

	@Override
	public final boolean equals(final Object thatValue) {
		if (thatValue instanceof PriceValue) {
			final PriceValue that = (PriceValue) thatValue;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public final double asDouble() {
		return mantissa() * Math.pow(10, exponent());
	}

}
