/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.barchart.util.value.api.Time;

final class TimeImpl implements Time {

	private final long millsUTC;
	private final TimeZone zone;

	TimeImpl(final long millsUTC, final TimeZone zone) {
		this.millsUTC = millsUTC;
		this.zone = zone;
	}

	@Override
	public final long millisecond() {
		return millsUTC;
	}

	@Override
	public TimeZone zone() {
		return zone;
	}

	@Override
	public Date asDate() {
		return new Date(millsUTC);
	}

	@Override
	public String format(final DateFormat format) {
		return format.format(asDate());
	}

	//

	@Override
	public final int compareTo(final Time that) {
		final long t1 = this.millisecond();
		final long t2 = that.millisecond();
		return t1 < t2 ? -1 : (t1 == t2 ? 0 : 1);
	}

	@Override
	public final int hashCode() {
		final long millis = this.millisecond();
		return (int) (millis ^ (millis >>> 32));
	}

	@Override
	public final boolean equals(final Object thatTime) {
		if (thatTime instanceof Time) {
			final Time that = (Time) thatTime;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public final String toString() {
		return String.format("Time > %30s", millisecond());
	}

	 @Override
	 public final boolean isNull() {
		 return this == Time.NULL;
	 }

}
