/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.value.api.Time;
import com.barchart.util.value.api.TimeInterval;

public class NulTimeInterval implements TimeInterval {

	// @Override
	// public TimeInterval freeze() {
	// return this;
	// }

	// @Override
	// public boolean isFrozen() {
	// return true;
	// }

	// @Override
	// public boolean isNull() {
	// return true;
	// }

	@Override
	public Time start() {
		return ValueConst.NULL_TIME;
	}

	// @Override
	// public long startAsMillis() {
	// return 0;
	// }

	@Override
	public Time stop() {
		return ValueConst.NULL_TIME;
	}

	@Override
	public TimeInterval copy() {
		return this;
	}

	// @Override
	// public long stopAsMillis() {
	// return 0;
	// }

}
