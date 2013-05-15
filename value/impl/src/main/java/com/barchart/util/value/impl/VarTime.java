/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.NotThreadSafe;

//16 bytes on 32 bit JVM
@Mutable
@NotThreadSafe
final class VarTime extends BaseTime {

	volatile long millisUTC;

	VarTime(final long millisUTC) {
		this.millisUTC = millisUTC;
	}

	@Override
	public final long millisecond() {
		return millisUTC;
	}

	// @Override
	// public final Time freeze() {
	// return newTime(millisUTC);
	// }

	// @Override
	// public final boolean isFrozen() {
	// return false;
	// }

}
