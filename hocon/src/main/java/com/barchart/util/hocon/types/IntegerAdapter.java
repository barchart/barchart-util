package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class IntegerAdapter implements TypeAdapter<Integer> {

	@Override
	public Integer convertValue(ConfigValue configValue) {
		return (Integer) configValue.unwrapped();
	}

}
