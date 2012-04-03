/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.provider.ValueBuilder;

public class TestValueModule {

	private static final Logger log = LoggerFactory
			.getLogger(TestValueModule.class);

	@Test
	public void testBuild() {

		J.registerModule(ValueModule.build());

		final Box boxIn = new Box();
		boxIn.decimal = ValueBuilder.newDecimal(234234, -3);
		boxIn.price = ValueBuilder.newPrice(123, -2);
		boxIn.size = ValueBuilder.newSize(1234567890123L);
		boxIn.text = ValueBuilder.newText("abc");
		boxIn.time = ValueBuilder.newTime(System.currentTimeMillis());
		log.debug("boxIn: {}", boxIn);

		final String json = J.intoText(boxIn);

		final Box boxOut = J.fromText(json, Box.class);
		log.debug("boxOut: {}", boxOut);

		assertEquals(boxIn.decimal, boxOut.decimal);
		assertEquals(boxIn.price, boxOut.price);
		assertEquals(boxIn.size, boxOut.size);
		assertEquals(boxIn.text, boxOut.text);
		assertEquals(boxIn.time, boxOut.time);

	}

}
