package com.barchart.util.value.api;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * 
 *
 */
public interface LocalTime extends Comparable<LocalTime>, Existential {

	/**
	 * 
	 * @return
	 */
	long millisecond();

	/**
	 * 
	 * @return
	 */
	Date asDate();

	/**
	 * 
	 * @param zone
	 * @return
	 */
	Date asDate(TimeZone zone);

	/**
	 * 
	 * @param format
	 * @return
	 */
	String format(DateFormat format);

	@Override
	boolean isNull();

	@Override
	int compareTo(LocalTime thatTime);

	@Override
	boolean equals(Object thatTime);

}
