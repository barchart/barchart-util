/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import static com.barchart.util.value.impl.ValueBuilder.newPrice;

import com.barchart.util.value.api.Price;

abstract class BasePrice extends BaseScaled<Price> implements Price {

	@Override
	protected Price result(final long mantissa, final int exponent) {
		return newPrice(mantissa, exponent);
	}

	@Override
	public final boolean equals(final Object thatValue) {
		if (thatValue instanceof Price) {
			final Price that = (Price) thatValue;
			return this.compareTo(that) == 0;
		}
		return false;
	}
	
	@Override
	public boolean isNull() {
		return this == Price.NULL;
	}

}
