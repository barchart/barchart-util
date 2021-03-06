/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

// 16 bytes on 32 bit JVM
final class DefPrice6 extends BasePrice {

	final long mantissa;

	DefPrice6(final long mantissa) {
		this.mantissa = mantissa;
	}

	@Override
	public final long mantissa() {
		return mantissa;
	}

	@Override
	public final int exponent() {
		return -6;
	}

}
