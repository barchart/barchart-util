/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;
import static com.barchart.util.values.provider.ValueConst.*;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.lang.ScaledDecimalValue;

@NotMutable
abstract class BaseDecimal extends
		ScaledDecimalValue<DecimalValue, DecimalValue> implements DecimalValue {

	@Override
	protected DecimalValue result(final long mantissa, final int exponent) {
		return newDecimal(mantissa, exponent);
	}

	@Override
	public final boolean isNull() {
		return this == NULL_DECIMAL;
	}

	@Override
	public final boolean equals(final Object thatValue) {
		if (thatValue instanceof DecimalValue) {
			DecimalValue that = (DecimalValue) thatValue;
			return this.compareTo(that) == 0;
		}
		return false;
	}

}
