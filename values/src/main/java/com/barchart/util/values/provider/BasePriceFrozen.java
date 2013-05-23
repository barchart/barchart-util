package com.barchart.util.values.provider;

import com.barchart.util.values.api.PriceValue;

public abstract class BasePriceFrozen extends BasePrice {

	@Override
	public PriceValue freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

}
