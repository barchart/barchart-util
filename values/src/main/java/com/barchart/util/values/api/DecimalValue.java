/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.api;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.lang.ScaledDecimal;

/** generic decimal value */
@NotMutable
public interface DecimalValue extends Value<DecimalValue>,
		ScaledDecimal<DecimalValue, DecimalValue> {

	double asDouble();
	
}
