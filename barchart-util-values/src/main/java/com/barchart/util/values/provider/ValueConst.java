/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.api.DecimalValue;
import com.barchart.util.values.api.PriceValue;
import com.barchart.util.values.api.SizeValue;
import com.barchart.util.values.api.TextValue;
import com.barchart.util.values.api.TimeValue;
import com.barchart.util.values.api.Value;
import com.barchart.util.values.size.JavaSize;

public final class ValueConst {

	private static final Logger log = LoggerFactory.getLogger(ValueConst.class);

	public static final void sizeReport(final Class<?> klaz) {

		final StringBuilder text = new StringBuilder(1024);

		text.append("##################################");
		text.append("\n");
		text.append("Instance Size Report");
		text.append("\n");

		final Field[] fieldArray = klaz.getFields();

		for (final Field field : fieldArray) {
			if (Value.class.isAssignableFrom(field.getType())) {
				try {
					String name = field.getName();
					Object value = field.get(null);
					int size = JavaSize.of(value);
					text.append(String.format("%-20s %,9d", name, size));
					text.append("\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		text.append("##################################");
		text.append("\n");

		log.debug("\n{}\n", text);

	}

	private ValueConst() {
	}

	public static final DecimalValue NULL_DECIMAL = //
	new DefDecimal(0, 0);

	public static final TextValue NULL_TEXT = //
	new NulText();

	public static final PriceValue NULL_PRICE = //
	new NulPrice();
	public static final PriceValue ZERO_PRICE = //
	new NulPrice();

	public static final SizeValue NULL_SIZE = //
	new NulSize();
	public static final SizeValue ZERO_SIZE = //
	new NulSize();

	public static final TimeValue NULL_TIME = //
	new NulTime();
	public static final TimeValue ZERO_TIME = //
	new NulTime();

	public static final SizeValue[] NULL_SIZE_ARRAY = new SizeValue[0];

	static {
		// sizeReport(ValueConst.class);
	}

}
