package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class BooleanAdapter implements TypeAdapter<Boolean> {

	@Override
	public Boolean convertValue(ConfigValue configValue) {
		return (Boolean) configValue.unwrapped();
	}

}
