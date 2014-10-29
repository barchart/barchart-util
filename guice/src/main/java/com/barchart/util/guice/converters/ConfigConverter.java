package com.barchart.util.guice.converters;

import com.barchart.util.guice.ValueConverter;
import com.google.inject.Binder;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public class ConfigConverter implements ValueConverter {

	@Override
	public void applyBindings(Binder binder, String key, ConfigValue value) {
		if (value.valueType() == ConfigValueType.OBJECT) {
			value.unwrapped();
		}
	}

}
