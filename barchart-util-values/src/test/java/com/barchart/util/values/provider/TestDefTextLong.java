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

public class TestDefTextLong {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLongConstructor() {

		final long value = 65 << 0 + 66 << 8;

		final TextValue text = new DefTextLong(value);

		final int size = JavaSize.of(text);

		// System.err.println("size=" + size);

		assertEquals(size, 16);
	}

	@Test
	public void testCharAt() {

		final long value = (0x41 << 0 * 8) + (0x42 << 1 * 8) + (0x43 << 2 * 8);

		// System.err.println("value=" + Long.toHexString(value));

		final TextValue text = new DefTextLong(value);

		assertEquals(text.length(), 3);

		assertEquals(text.charAt(0), 'A');
		assertEquals(text.charAt(1), 'B');
		assertEquals(text.charAt(2), 'C');

		// System.err.println("size=" + size);

	}

	@Test
	public void testHashCode() {

		final long value = (0x41 << 0 * 8) + (0x42 << 1 * 8) + (0x43 << 2 * 8);

		// System.err.println("value=" + Long.toHexString(value));

		final TextValue text = new DefTextLong(value);

		assertEquals(text.hashCode(), "ABC".hashCode());

		assertTrue(text.equals("ABC"));

	}

	@Test
	public void testSubString() {

		final long value = (0x41 << 0 * 8) + (0x42 << 1 * 8) + (0x43 << 2 * 8)
				+ (0x44 << 3 * 8);

		// System.err.println("value=" + Long.toHexString(value));

		//

		final TextValue text0 = new DefTextLong(value);
		// System.err.println("text0=" + text0);
		assertEquals(text0.hashCode(), "ABCD".hashCode());

		//

		final CharSequence seq1 = text0.subSequence(0, 1);
		// System.err.println("text1=" + seq1);
		assertTrue(seq1.equals("A"));

		//

		final CharSequence seq2 = text0.subSequence(1, 3);
		// System.err.println("text2=" + seq2);
		assertTrue(seq2.equals("BC"));
		assertEquals(seq2.length(), 2);

		//

		final CharSequence seq3 = text0.subSequence(3, 4);
		// System.err.println("text3=" + seq3);
		assertTrue(seq3.equals("D"));

	}

	@Test
	public void testArrayConstructor() {

		final byte[] array = new byte[] { 'A', 'B', 'C' };

		// System.err.println("value=" + Long.toHexString(value));

		final TextValue text = new DefTextLong(array);

		assertEquals(text.length(), 3);

		assertEquals(text.charAt(0), 'A');
		assertEquals(text.charAt(1), 'B');
		assertEquals(text.charAt(2), 'C');

		// System.err.println("size=" + size);

	}

	@Test
	public void testStringConstructor() {

		final String string = "ABC";

		// System.err.println("value=" + Long.toHexString(value));

		final TextValue text = new DefTextLong(string);

		assertEquals(text.length(), 3);

		assertEquals(text.charAt(0), 'A');
		assertEquals(text.charAt(1), 'B');
		assertEquals(text.charAt(2), 'C');

		// System.err.println("size=" + size);

		assertEquals(new DefTextLong("01234567"), "01234567");

		assertEquals(new DefTextLong("01234567").hashCode(),
				"01234567".hashCode());

	}

	@Test
	public void testToUpperCase() {

		final TextValue text1 = new DefTextLong("aBcD1?xY");
		final TextValue text2 = text1.toUpperCase();
		// System.err.println("text1=" + text1);
		// System.err.println("text2=" + text2);

		assertEquals(new DefTextLong("").toUpperCase(), "");
		assertEquals(new DefTextLong("a").toUpperCase(), "A");
		assertEquals(new DefTextLong("aBcD1?xY").toUpperCase(), "ABCD1?XY");

	}

	@Test
	public void testToLowerCase() {

		assertEquals(new DefTextLong("").toLowerCase(), "");

		assertEquals(new DefTextLong("a").toLowerCase(), "a");
		assertEquals(new DefTextLong("A").toLowerCase(), "a");

		assertEquals(new DefTextLong("aBcD1?xY").toLowerCase(), "abcd1?xy");

	}

}
