/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Price;

class PriceValueSer extends ScaledValueSer<Price, Decimal> {

	@SuppressWarnings("unchecked")
	protected <V extends Price> PriceValueSer(Class<V> klaz) {
		super((Class<Price>) klaz);
	}

}
