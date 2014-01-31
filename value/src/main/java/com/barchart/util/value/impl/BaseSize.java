/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.value.api.Size;

abstract class BaseSize extends BaseScaled<Size> implements Size {

	@Override
	protected Size result(final long mantissa, final int exponent) {
		return vals.newSize(mantissa, exponent);
	}

	@Override
	public final boolean equals(final Object thatValue) {
		if (thatValue instanceof Size) {
			final Size that = (Size) thatValue;
			return this.compareTo(that) == 0;
		}
		return false;
	}
	
	@Override
	public boolean isNull() {
		return this == Size.NULL;
	}

}
