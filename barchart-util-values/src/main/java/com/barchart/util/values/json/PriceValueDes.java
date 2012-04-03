/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.provider.ValueBuilder;

class PriceValueDes extends ScaledValueDes<PriceValue, DecimalValue> {

	static Logger log = LoggerFactory.getLogger(PriceValueDes.class);

	protected PriceValueDes(Class<PriceValue> klaz) {
		super(klaz);
	}

	@Override
	protected PriceValue newValue(long mantissa, int exponent) {
		return ValueBuilder.newPrice(mantissa, exponent);
	}

}
