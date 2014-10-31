package com.barchart.util.guice.converters;

import java.util.HashMap;
import java.util.List;

import com.barchart.util.guice.ValueConverter;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public final class ConfigListConverter implements ValueConverter {

	@SuppressWarnings("unchecked")
	@Override
	public void applyBindings(Binder binder, String key, ConfigValue value) {
		if (value.valueType() == ConfigValueType.LIST) {
			List<Object> unwrapped = (List<Object>) value.unwrapped();
			if (unwrapped.isEmpty() || unwrapped.get(0) instanceof HashMap) {
				binder.bind(new TypeLiteral<List<Config>>() {
				}).annotatedWith(Names.named(key)).toInstance((List<Config>) value.atKey(key).getConfigList(key));
			}
		}
	}

}
