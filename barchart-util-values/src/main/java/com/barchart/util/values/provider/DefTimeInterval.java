/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import com.barchart.util.values.api.TimeInterval;
import com.barchart.util.values.api.TimeValue;

public class DefTimeInterval extends ValueFreezer<TimeInterval> implements TimeInterval {

	final TimeValue start;
	final TimeValue stop;
	
	DefTimeInterval(final TimeValue start, final TimeValue stop) {
		this.start = start;
		this.stop = stop;
	}
	
	@Override
	public boolean isNull() {
		return this == ValueConst.NULL_TIME_INTERVAL;
	}

	@Override
	public TimeValue start() {
		return start.freeze();
	}

	@Override
	public long startAsMillis() {
		return start.asMillisUTC();
	}

	@Override
	public TimeValue stop() {
		return stop.freeze();
	}

	@Override
	public long stopAsMillis() {
		return stop.asMillisUTC();
	}

}
