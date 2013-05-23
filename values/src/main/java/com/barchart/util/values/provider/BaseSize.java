/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static com.barchart.util.value.impl.ValueBuilder.newSize;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.math.MathExtra;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.impl.BaseScaled;
import com.barchart.util.values.api.SizeValue;

@NotMutable
abstract class BaseSize extends BaseScaled<Size, Decimal> implements SizeValue {

	//

	protected abstract SizeValue returnSize(long value);

	@Override
	public abstract long asLong();

	//
	
	@Override
	protected Size result(final long mantissa, final int exponent) {
		return newSize(mantissa, exponent);
	}

	@Deprecated
	@Override
	public final int asInt() {
		return MathExtra.castLongToInt(asLong());
	}

	@Override
	public final boolean equals(Object thatSize) {
		if (thatSize instanceof SizeValue) {
			SizeValue that = (SizeValue) thatSize;
			return this.asLong() == that.asLong();
		}
		return false;
	}

	@Override
	public final String toString() {
		return String.format("Size > %9d", asLong()); // 16
	}

	@Override
	public final boolean isNull() {
		return this == ValueConst.NULL_SIZE;
	}

}
