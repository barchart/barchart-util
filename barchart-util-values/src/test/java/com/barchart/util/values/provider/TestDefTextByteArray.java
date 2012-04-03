/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barchart.util.bench.size.JavaSize;
import com.barchart.util.values.api.TextValue;

public class TestDefTextByteArray {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSize() {

		final TextValue text0 = new DefTextByteArray(new byte[] {});

		final int size0 = JavaSize.of(text0);

		// System.err.println("size=" + size);

		assertEquals(size0, 24);

		final TextValue text1 = new DefTextByteArray(
				new byte[] { 0, 1, 2, 3, 4 });

		final int size1 = JavaSize.of(text1);

		// System.err.println("size=" + size);

		assertEquals(size1, 24 + 5);

	}

	@Test
	public void testHashCode() {

		final byte[] array = new byte[] { 65, 66, 67 };

		final TextValue text = new DefTextByteArray(array);

		final String string = text.toString();

		// System.err.println("text=" + text);

		assertTrue(text.equals(string));
		// due to string expectin only string
		assertFalse(string.equals(text));
		assertEquals(text.hashCode(), string.hashCode());

		//

		assertTrue(text.equals("ABC"));
		assertEquals(text.hashCode(), "ABC".hashCode());

		//

		assertTrue("ABC".compareTo("AB") > 0);
		assertTrue(text.compareTo("AB") > 0);

		assertTrue("ABC".compareTo("ABc") < 0);
		assertTrue(text.compareTo("ABc") < 0);

		assertTrue("ABC".compareTo("ABCD") < 0);
		assertTrue(text.compareTo("ABCD") < 0);

	}

	@Test
	public void testSubString() {

		final byte[] array = new byte[] { 65, 66, 67 };

		final TextValue text = new DefTextByteArray(array);

		final CharSequence seq1 = text.subSequence(0, 1);
		assertTrue(seq1.equals("A"));

		final CharSequence seq2 = text.subSequence(1, 3);
		assertTrue(seq2.equals("BC"));

	}

	@Test
	public void testToUpperCase() {

		assertEquals(new DefTextByteArray("").toUpperCase(), "");

		assertEquals(new DefTextByteArray("x").toUpperCase(), "X");

		assertEquals(new DefTextByteArray("aB1c3D-Az23").toUpperCase(),
				"AB1C3D-AZ23");

	}

	@Test
	public void testToLowerCase() {

		assertEquals(new DefTextByteArray("").toLowerCase(), "");

		assertEquals(new DefTextByteArray("x").toLowerCase(), "x");

		assertEquals(new DefTextByteArray("X").toLowerCase(), "x");

		assertEquals(new DefTextByteArray("aB1c3D-Az23").toLowerCase(),
				"ab1c3d-az23");

	}

}
