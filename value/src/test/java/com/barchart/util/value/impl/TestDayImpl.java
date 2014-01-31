package com.barchart.util.value.impl;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import com.barchart.util.value.api.Day;

public class TestDayImpl {

	public static final long TIME = 1391166671001l; // Fri, 31 Jan 2014 11:11:11 GMT
	public static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	@Test
	public void testConstructor() {

		final Day day = new DayImpl(TIME);

		final TimeZone chicago = TimeZone.getTimeZone("CST");
		final DateFormat chiFormat = new SimpleDateFormat(FORMAT);
		chiFormat.setTimeZone(chicago);

		final Calendar chiCal = Calendar.getInstance(chicago);
		chiCal.setTime(day.asDate(chicago));

		assertTrue(chiCal.get(Calendar.HOUR_OF_DAY) == 0);
		assertTrue(chiCal.get(Calendar.MINUTE) == 0);
		assertTrue(chiCal.get(Calendar.SECOND) == 0);

		final TimeZone ny = TimeZone.getTimeZone("EST");
		final DateFormat nyFormat = new SimpleDateFormat(FORMAT);
		nyFormat.setTimeZone(ny);

		final Calendar nyCal = Calendar.getInstance(ny);
		nyCal.setTime(day.asDate(ny));

		assertTrue(nyCal.get(Calendar.HOUR_OF_DAY) == 0);
		assertTrue(nyCal.get(Calendar.MINUTE) == 0);
		assertTrue(nyCal.get(Calendar.SECOND) == 0);

	}

}
