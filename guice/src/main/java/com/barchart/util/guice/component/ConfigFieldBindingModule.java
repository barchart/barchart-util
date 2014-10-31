package com.barchart.util.guice.component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.ValueConverter;
import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public final class ConfigFieldBindingModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigFieldBindingModule.class);
	
	private final Config config;

	private final String pathPrefix;

	private final List<ValueConverter> valueConverters;

	public ConfigFieldBindingModule(Config config, String pathPrefix, List<ValueConverter> valueConverters) {
		this.config = config;
		this.pathPrefix = pathPrefix;
		this.valueConverters = valueConverters;
	}

	@Override
	protected void configure() {
		UniqueObjectPathSet objectPathSet = new UniqueObjectPathSet();
		
		for (Entry<String, ConfigValue> entry : config.entrySet()) {
			objectPathSet.add(entry.getKey());
			bindEntry(entry.getKey(), entry.getValue());
		}
		
		for (String objectPath : objectPathSet) {
			Config objectConfig = config.getConfig(objectPath);
			System.out.println("Config root:\n" + objectConfig.root());
			bindEntry(objectPath, objectConfig.root());
		}
		
	}

	private void bindEntry(String path, ConfigValue configValue) {
		String key = pathPrefix + path;
		logger.info("Binding key: " + key);
		for (ValueConverter converters : valueConverters) {
			converters.applyBindings(binder(), key, configValue);
		}
	}


}
