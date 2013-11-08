/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;

@NotMutable
abstract class BaseTime implements Time {

	static final String UTC = "UTC".intern();

	@Override
	public abstract long millisecond();

	@Override
	public String zone() {
		return UTC;
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
