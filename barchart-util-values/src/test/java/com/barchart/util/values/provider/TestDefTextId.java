/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestDefTextId {

	protected void setUp() throws Exception {
	}

	protected void tearDown() throws Exception {
	}

	@Test
	public void text() {

		final long value = Long.MAX_VALUE - 7;

		final String string = "" + value;

		final DefTextId textId = new DefTextId(value);

		assertTrue(textId.equals(string));

		assertFalse(string.equals(textId));

		assertEquals(textId.hashCode(), string.hashCode());

		//

		// 121
		// 122
		assertTrue(new DefTextId(121).compareTo(new DefTextId(122)) < 0);

	}

}
