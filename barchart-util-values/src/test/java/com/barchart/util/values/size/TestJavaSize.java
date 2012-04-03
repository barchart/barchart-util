/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.size;

import static com.barchart.util.values.size.JavaSize.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJavaSize {

	private static final Logger log = LoggerFactory
			.getLogger(TestJavaSize.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testX() {

		assertEquals(of(null), 0);

		assertEquals(of(true), 1);

		assertEquals(of((byte) 37), 1);

		assertEquals(of('x'), 2);

		assertEquals(of((short) 37), 2);

		assertEquals(of(37), 4);

		assertEquals(of(37L), 8);

		assertEquals(of(37.0f), 4);

		assertEquals(of(37.0), 8);

		assertEquals(of(new boolean[0]), OBJECT_SIZE);

		assertEquals(of(new byte[10]), OBJECT_SIZE + 10);

		assertEquals(of(new char[10]), OBJECT_SIZE + 10 * 2);

		assertEquals(of(new char[8][9]), OBJECT_SIZE + 8
				* (REFERENCE_SIZE + OBJECT_SIZE + 9 * 2));

		// assertEquals(of(new double[5][6][7]), OBJECT_SIZE + 5*(REFERENCE_SIZE
		// + OBJECT_SIZE + 6));

		assertEquals(of(new int[0][10]), OBJECT_SIZE);

		assertEquals(of(new Object()), OBJECT_SIZE);

		assertEquals(of(new String()), STRING_BASE);

		assertEquals(of(new String[100]), OBJECT_SIZE + 100 * REFERENCE_SIZE);

		assertEquals(of(new String[7][8]), OBJECT_SIZE + 7
				* (REFERENCE_SIZE + OBJECT_SIZE + 8 * REFERENCE_SIZE));

		final Object ov[] = new Object[3];
		ov[0] = new byte[10];
		ov[1] = null;
		ov[2] = new double[10];
		assertEquals(of(ov), 90 + 3 * REFERENCE_SIZE + 3 * OBJECT_SIZE);

		final String sv[] = new String[10];
		for (int i = 0; i < 10; i++) {
			sv[i] = new String();
		}
		assertEquals(of(sv), OBJECT_SIZE + 10 * REFERENCE_SIZE + 10
				* STRING_BASE);

		assertEquals(of(new Integer(37)), 4 + OBJECT_SIZE + 4);

		assertEquals(of(new test_Class1()), test_Class1.SIZE);

		assertEquals(of(new test_Class2()), test_Class2.SIZE);

		assertEquals(of(new test_Class3()), test_Class3.SIZE);

		assertEquals(of(new test_Class7()), test_Class7.SIZE);

	}

	@Test
	public void test0() {
		assertEquals(of(new Object()), OBJECT_SIZE);
	}

	@Test
	public void test1() {

		Object item;
		int size;

		item = new test_Class_A();
		size = of(item);

		assertEquals(size, OBJECT_SIZE);

	}

	@Test
	public void test2() {

		Object item;
		int size;

		item = new test_Class_B();
		size = of(item);

		assertEquals(size, OBJECT_SIZE + 4 + 4);

	}

	@Test
	public void test3() {

		Number item;
		int size;

		item = new Integer(37);
		size = of(item);
		assertEquals(size, OBJECT_SIZE + 4 + 4);

		item = new Double(37);
		size = of(item);
		assertEquals(size, OBJECT_SIZE + 8);

	}

	final static int STRING_BASE = OBJECT_SIZE + 3 * 4 + REFERENCE_SIZE
			+ OBJECT_SIZE;

	@Test
	public void test4() {

		String item;
		int size;

		item = new String();
		size = of(item);
		assertEquals(size, STRING_BASE + 0);

		item = new String("");
		size = of(item);
		assertEquals(size, STRING_BASE + 0);

		item = new String("1");
		size = of(item);
		assertEquals(size, STRING_BASE + 2);

		item = new String("12");
		size = of(item);
		assertEquals(size, STRING_BASE + 4);

		item = new String("123");
		size = of(item);
		assertEquals(size, STRING_BASE + 6);

	}

	@Test
	public void test5() {

		final int row = 3;
		final int col = 7;

		final int sizeL0 = of('A');
		final int sizeL1 = OBJECT_SIZE + col * sizeL0;
		final int sizeL2 = OBJECT_SIZE + row * (REFERENCE_SIZE + sizeL1);

		final char[][] L2 = new char[row][col];
		assertEquals(of(L2), sizeL2);

		for (final char[] L1 : L2) {
			assertNotNull(L1);
			assertEquals(of(L1), sizeL1);
			for (final char L0 : L1) {
				assertEquals(0, L0);
				assertEquals(of(L0), 2);
			}
		}

	}

	@Test
	public void testSelfReference1() {

		final test_Class_C self = new test_Class_C();

		self.self = self;

		Object item;
		int size;

		item = self;
		size = of(item);
		assertEquals(size, OBJECT_SIZE + REFERENCE_SIZE + 4);

	}

	@Test
	public void testSelfReference2() {

		final Object[] self = new Object[1];

		self[0] = self;

		Object item;
		int size;

		item = self;
		size = of(item);
		assertEquals(size, OBJECT_SIZE + REFERENCE_SIZE);

	}

}
