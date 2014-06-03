package com.barchart.util.value.impl;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Test;

import com.barchart.util.value.api.Time;

public class TestTimeImpl {

	public static final long TIME = 1391180400000l; // Fri Jan 31 9am CST

	@Test
	public void testFormat() {

		final TimeZone local = TimeZone.getDefault();

		final Time time = new TimeImpl(TIME, local);

		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		assertTrue(time.format(format).equals("2014-01-31T10:00:00"));

		final TimeZone utc = TimeZone.getTimeZone("UTC");
		format.setTimeZone(utc);

		assertTrue(time.format(format).equals("2014-01-31T15:00:00"));

	}

	@Test
	public void testEquals() {

		final TimeZone local = TimeZone.getDefault();
		final TimeZone utc = TimeZone.getTimeZone("UTC");

		final Time time1 = new TimeImpl(TIME, local);
		final Time time2 = new TimeImpl(TIME, utc);

		assertTrue(time1.equals(time2));

	}

}
