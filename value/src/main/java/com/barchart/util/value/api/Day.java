package com.barchart.util.value.api;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;


public interface Day extends LocalTime {

	@Override
	long millisecond();

	@Override
	Date asDate();

	@Override
	Date asDate(TimeZone zone);

	@Override
	String format(DateFormat format);

	@Override
	boolean isNull();

	@Override
	int compareTo(LocalTime thatTime);

	@Override
	boolean equals(Object thatTime);

}
