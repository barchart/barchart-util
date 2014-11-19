package com.barchart.util.test.guice;

import com.barchart.util.guice.ConfigBinder;
import com.barchart.util.guice.Filetypes;
import com.barchart.util.guice.ValueConverterTool;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

/**
 * Module that provides a component for injection using the specified config for use in unit testing.
 */
public class TestConfigModule extends AbstractModule {

	private final Config config;

	public TestConfigModule(final String config_) {
		config = ConfigFactory.parseString(config_,
				ConfigParseOptions.defaults().setOriginDescription("/" + Filetypes.DEFAULT_CONFIG_FILE));
	}

	public TestConfigModule(final Config config_) {
		config = config_;
	}

	@Override
	protected void configure() {

		final ConfigBinder binder = new ConfigBinder(binder(), ValueConverterTool.defaultValueConverterTool());

		final String fileName = Filetypes.getSimpleName(config);

		if (Filetypes.isDefaultConfigFile(config)) {
			binder.applyBindings(config);
			bind(Config.class).toInstance(config);
			bind(Config.class).annotatedWith(Names.named("/")).toInstance(config);
		}

		binder.applyBindings(config, fileName + "/");

	}

}

