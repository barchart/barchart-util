/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.ValueFactory;

class TimeIntervalImpl implements TimeInterval {

	// USE INTERNAL CONSTRUCTORS NOT FACTORY
	private static final ValueFactory vals = new ValueFactoryImpl();

	final long start;
	final long stop;

	TimeIntervalImpl(final Time start, final Time stop) {
		this.start = start.millisecond();
		this.stop = stop.millisecond();
	}

	TimeIntervalImpl(final long start, final long stop) {
		this.start = start;
		this.stop = stop;
	}

	@Override
	public Time start() {
		return vals.newTime(start);
	}

	@Override
	public Time stop() {
		return vals.newTime(stop);
	}

	@Override
	public boolean equals(final Object o) {

		if (o == null) {
			return false;
		}

		if (this == o) {
			return true;
		}

		if (!(o instanceof TimeInterval)) {
			return false;
		}

		final TimeInterval tI = (TimeInterval) o;

		return (start().equals(tI.start())) && (stop().equals(tI.stop()));

	}
	
	@Override
	public boolean isNull() {
		return this == TimeInterval.NULL;
	}

}
