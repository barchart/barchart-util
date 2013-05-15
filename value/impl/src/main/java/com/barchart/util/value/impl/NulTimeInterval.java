/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import temp.TimeInterval;
import temp.TimeValue;

public class NulTimeInterval implements TimeInterval {

	@Override
	public TimeInterval freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public TimeValue start() {
		return ValueConst.NULL_TIME;
	}

	@Override
	public long startAsMillis() {
		return 0;
	}

	@Override
	public TimeValue stop() {
		return ValueConst.NULL_TIME;
	}

	@Override
	public long stopAsMillis() {
		return 0;
	}

}
