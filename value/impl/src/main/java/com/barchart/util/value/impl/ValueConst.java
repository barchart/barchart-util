/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

/**
 * NOTE: this class is bundle-private in OSGI.
 */
public final class ValueConst {

	private static final Logger log = LoggerFactory.getLogger(ValueConst.class);

	private ValueConst() {
	}

	public static final Decimal NULL_DECIMAL = //
	new NulDecimal();
	public static final Decimal ZERO_DECIMAL = //
	new NulDecimal();

	public static final Price NULL_PRICE = //
	new NulPrice();
	public static final Price ZERO_PRICE = //
	new NulPrice();

	public static final Size NULL_SIZE = //
	new NulSize();
	public static final Size ZERO_SIZE = //
	new NulSize();

	public static final Time NULL_TIME = //
	new NulTime();
	public static final Time ZERO_TIME = //
	new NulTime();

	public static final TimeInterval NULL_TIME_INTERVAL = //
	new NulTimeInterval();

	public static final Size[] NULL_SIZE_ARRAY = new Size[0];

	static {
		// sizeReport(ValueConst.class);
	}

}
