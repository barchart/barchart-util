package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class FloatAdapter implements TypeAdapter<Float> {

	@Override
	public Float convertValue(ConfigValue configValue) {
		return ((Double) configValue.unwrapped()).floatValue();
	}

}
