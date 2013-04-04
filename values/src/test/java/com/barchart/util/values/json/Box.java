/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import org.codehaus.jackson.annotate.JsonProperty;

import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;

public class Box {

	@JsonProperty("decimal")
	DecimalValue decimal;

	@JsonProperty("price")
	PriceValue price;

	@JsonProperty("size")
	SizeValue size;

	@JsonProperty("text")
	TextValue text;

	@JsonProperty("time")
	TimeValue time;

	@Override
	public String toString() {
		return J.intoText(this);
	}

}
