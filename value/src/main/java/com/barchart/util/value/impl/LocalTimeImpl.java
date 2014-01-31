package com.barchart.util.value.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.barchart.util.value.api.LocalTime;

class LocalTimeImpl implements LocalTime {

	private final long millis;

	LocalTimeImpl(final long millis) {
		this.millis = millis;
	}

	@Override
	public long millisecond() {
		return millis;
	}

	@Override
	public Date asDate() {
		return new Date(millis - TimeZone.getDefault().getOffset(millis));
	}

	@Override
	public Date asDate(final TimeZone zone) {
		return new Date(millis - zone.getOffset(millis));
	}

	@Override
	public String format(final DateFormat format) {
		return format.format(new Date(millis - format.getTimeZone().getOffset(millis)));
	}

	@Override
	public int compareTo(final LocalTime that) {
		final long t1 = this.millisecond();
		final long t2 = that.millisecond();
		return t1 < t2 ? -1 : (t1 == t2 ? 0 : 1);
	}

	@Override
	public boolean equals(final Object thatTime) {
		if (thatTime instanceof LocalTime) {
			final LocalTime that = (LocalTime) thatTime;
			return this.compareTo(that) == 0;
		}
		return false;
	}

	@Override
	public boolean isNull() {
		return false;
	}

}
