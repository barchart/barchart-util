package com.barchart.util.guice.converters;

import com.barchart.util.guice.ValueConverter;
import com.google.inject.Binder;
import com.google.inject.name.Names;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public class FloatConverter implements ValueConverter {

	@Override
	public void applyBindings(Binder binder, String key, ConfigValue value) {
		if (value.valueType() == ConfigValueType.NUMBER) {
			binder.bind(Float.class).annotatedWith(Names.named(key)).toInstance(((Number) value.unwrapped()).floatValue());
		}
	}

}
