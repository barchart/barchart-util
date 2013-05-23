/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;

import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.value.api.Time;
import com.barchart.util.values.api.TimeValue;

//16 bytes on 32 bit JVM
@Mutable
@NotThreadSafe
final class VarTime extends BaseTime {

	private volatile long millisUTC;

	VarTime(final long millisUTC) {
		this.millisUTC = millisUTC;
	}

	@Override
	public final long millisecond() {
		return millisUTC;
	}

	@Override
	public final TimeValue freeze() {
		return newTime(millisUTC);
	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

	@Override
	public String zone() {
		return null;
	}

	@Override
	public Time copy() {
		return this.freeze();
	}

}
