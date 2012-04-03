/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.api.PriceValue;

class PriceValueSer extends ScaledValueSer<PriceValue, DecimalValue> {

	protected PriceValueSer(Class<PriceValue> klaz) {
		super(klaz);
	}

}
