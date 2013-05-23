package com.barchart.util.values.provider;

import com.barchart.util.values.api.SizeValue;

public abstract class BaseSizeFrozen extends BaseSize {

	@Override
	public SizeValue freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

}
