/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.value.api.Price;

// 8 bytes on 32 bit JVM
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
	protected Price result(final long mantissa, final int exponent) {
		return vals.newPrice(mantissa, exponent);
	}

}
