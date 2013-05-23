/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static com.barchart.util.values.provider.ValueBuilder.newPrice;
import static com.barchart.util.values.provider.ValueConst.NULL_PRICE;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.impl.BaseScaled;
import com.barchart.util.values.api.PriceValue;

@NotMutable
abstract class BasePrice extends BaseScaled<Price, Decimal>
		implements PriceValue, Price {

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
			PriceValue that = (PriceValue) thatValue;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public final double asDouble() {
		return mantissa() * Math.pow(10, exponent());
	}
	
}
