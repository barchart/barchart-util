/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.api;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.lang.ScaledDecimal;

/** should be used for prices only */
@NotMutable
public interface PriceValue extends Value<PriceValue>,
		ScaledDecimal<PriceValue, DecimalValue> {

	double asDouble();
	
}