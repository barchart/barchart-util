package com.barchart.util.value.provider;

import com.barchart.util.loader.Producer;
import com.barchart.util.value.api.Factory;
import com.barchart.util.value.impl.FactoryImpl;

/**
 * Value factory producer and implementation.
 */
public class FactoryProvider implements Producer<Factory> {

	static final Factory INSTANCE = new FactoryImpl();

	@Override
	public Factory produce() {
		return INSTANCE;
	}

}
