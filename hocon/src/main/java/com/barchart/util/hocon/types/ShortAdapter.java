package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class ShortAdapter implements TypeAdapter<Short> {

	@Override
	public Short convertValue(ConfigValue configValue) {
		return ((Integer) configValue.unwrapped()).shortValue();
	}

}
