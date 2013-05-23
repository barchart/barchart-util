/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.provider.ValueBuilder;

class DecimalValueDes extends ScaledValueDes<Decimal, Decimal> {

	static Logger log = LoggerFactory.getLogger(DecimalValueDes.class);

	@SuppressWarnings("unchecked")
	protected <V extends Decimal> DecimalValueDes(Class<V> klaz) {
		super((Class<Decimal>) klaz);
	}

	@Override
	protected DecimalValue newValue(long mantissa, int exponent) {
		return ValueBuilder.newDecimal(mantissa, exponent);
	}

}
