/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;

//16 bytes on 32 bit JVM
@NotMutable
final class DefTime extends BaseTime {

	private final long millisUTC;

	DefTime(final long millisUTC) {
		this.millisUTC = millisUTC;
	}

	@Override
	public final long millisecond() {
		return millisUTC;
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
