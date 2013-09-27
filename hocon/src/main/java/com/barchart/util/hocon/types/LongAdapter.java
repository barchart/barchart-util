package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class LongAdapter implements TypeAdapter<Long> {

	@Override
	public Long convertValue(ConfigValue configValue) {
		return ((Integer) configValue.unwrapped()).longValue();
	}

}
