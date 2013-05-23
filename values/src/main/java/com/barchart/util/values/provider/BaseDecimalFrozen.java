package com.barchart.util.values.provider;

import com.barchart.util.values.api.DecimalValue;

public abstract class BaseDecimalFrozen extends BaseDecimal {

	@Override
	public DecimalValue freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

}
