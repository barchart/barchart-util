package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class DoubleAdapter implements TypeAdapter<Double> {

	@Override
	public Double convertValue(ConfigValue configValue) {
		return ((Double) configValue.unwrapped());
	}

}
