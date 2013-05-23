/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.PriceValue;

// 8 bytes on 32 bit JVM
@NotMutable
final class NulPrice extends BasePrice {

	@Override
	public long mantissa() {
		return 0;
	}

	@Override
	public int exponent() {
		return 0;
	}

	@Override
	protected PriceValue result(long mantissa, int exponent) {
		return ValueBuilder.newPrice(mantissa, exponent);
	}

	@Override
	public PriceValue freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

}
