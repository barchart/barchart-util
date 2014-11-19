package com.barchart.util.test.guice;

import com.barchart.util.guice.ConfigBinder;
import com.barchart.util.guice.ValueConverterTool;
import com.google.inject.PrivateModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

/**
 * Module that provides a component for injection using the specified config for use in unit testing.
 */
public class TestComponentModule extends PrivateModule {

	private final Class<?> type;
	private final Config config;

	public TestComponentModule(final Class<?> type_, final String config_) {
		type = type_;
		config = ConfigFactory.parseString(config_,
				ConfigParseOptions.defaults().setOriginDescription("/" + type_.getSimpleName() + ".component"));
	}

	public TestComponentModule(final Class<?> type_, final Config config_) {
		type = type_;
		config = config_;
	}

	@Override
	protected void configure() {
		final ConfigBinder binder = new ConfigBinder(binder(), ValueConverterTool.defaultValueConverterTool());
		binder.applyBindings(config, "#");
		bind(type);
		expose(type);
	}

}

