package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Time extends Comparable<Time>, Copyable<Time> {

	String timeZoneCode();

	/**
	 * milliseconds since January 1, 1970, 00:00:00 GMT
	 **/
	long asMillisUTC();

	@Override
	int compareTo(Time thatTime);

	@Override
	int hashCode();

	@Override
	boolean equals(Object thatTime);

}
