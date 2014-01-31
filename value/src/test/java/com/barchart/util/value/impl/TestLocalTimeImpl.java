package com.barchart.util.value.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import com.barchart.util.value.api.LocalTime;

public class TestLocalTimeImpl {

	public static final long TIME = 1391158800000l; // 9 am GMT
	public static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	@Test
	public void testDates() {

		final LocalTime time = new LocalTimeImpl(TIME);

		final TimeZone chicago = TimeZone.getTimeZone("CST");
		final DateFormat chiFormat = new SimpleDateFormat(FORMAT);
		chiFormat.setTimeZone(chicago);

		final TimeZone ny = TimeZone.getTimeZone("EST");
		final DateFormat nyFormat = new SimpleDateFormat(FORMAT);
		nyFormat.setTimeZone(ny);

		/* Test that dates from different zones are not equal */
		final Date chiDate = time.asDate(chicago);
		final Date nyDate = time.asDate(ny);
		assertFalse(chiDate.equals(nyDate));

		/* The formats for different time zones should parse to the same time, 9am */
		String formChi = chiFormat.format(time.asDate(chicago));
		String formNy = nyFormat.format(time.asDate(ny));
		assertTrue(formChi.equals(formNy));

		/* The strings from the format method should be the same regardless of time zone */
		formChi = time.format(chiFormat);
		formNy = time.format(nyFormat);
		assertTrue(formChi.equals(formNy));

	}

}
