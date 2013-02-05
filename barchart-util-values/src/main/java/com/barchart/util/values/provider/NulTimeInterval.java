package com.barchart.util.values.provider;

import com.barchart.util.values.api.TimeInterval;
import com.barchart.util.values.api.TimeValue;

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
