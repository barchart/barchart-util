package com.barchart.util.value.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.barchart.util.value.api.TimeInterval;

public class TestDefTimeInterval {

	public static final long START1 = 0l;
	public static final long START2 = 1l;
	public static final long END1 = 100;;
	public static final long END2 = 101;

	public static final TimeInterval NULL = TimeInterval.NULL;
	public static final TimeInterval TEST1 = new TimeIntervalImpl(START1, END1);
	public static final TimeInterval TEST2 = new TimeIntervalImpl(START1, END1);
	public static final TimeInterval TEST3 = new TimeIntervalImpl(START2, END1);
	public static final TimeInterval TEST4 = new TimeIntervalImpl(START1, END2);

	@Test
	public void testDefTimeInterval() {

		assertFalse(NULL.equals(TEST1));
		assertFalse(TEST1.equals(NULL));

		assertFalse(TEST1.equals(TEST3));
		assertFalse(TEST1.equals(TEST4));

		assertTrue(TEST1.equals(TEST2));

	}

}
