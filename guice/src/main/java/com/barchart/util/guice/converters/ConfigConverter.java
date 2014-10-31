package com.barchart.util.guice.converters;

import com.barchart.util.guice.ValueConverter;
import com.google.inject.Binder;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public class ConfigConverter implements ValueConverter {

	private static final String TEMPORARY_CONFIG_PATH = "configPath";

	@Override
	public void applyBindings(Binder binder, String key, ConfigValue value) {
		if (value.valueType() == ConfigValueType.OBJECT) {
			Config configInstance = value.atPath(TEMPORARY_CONFIG_PATH).getConfig(TEMPORARY_CONFIG_PATH);
			binder.bind(Config.class).annotatedWith(Names.named(key)).toInstance(configInstance);
		}
	}

}
