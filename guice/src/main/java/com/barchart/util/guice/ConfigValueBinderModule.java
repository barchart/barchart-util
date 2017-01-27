package com.barchart.util.guice;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;

final class ConfigValueBinderModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigValueBinderModule.class);

	@Inject
	private ConfigResources resources;

	@Inject
	private ConfigBinder configBinder;

	ConfigValueBinderModule() {
	}

	@Override
	protected void configure() {
		// FIXME: ? hack to get correct injector in ModuleLoaderModule
		bind(ModuleLoaderModule.class);
		try {
			for (final Config config : resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
				logger.debug("Binding values from config file: " + config.origin().description());
				final String fileName = Filetypes.getSimpleName(config);
				if (Filetypes.isDefaultConfigFile(config)) {
					configBinder.applyBindings(binder(), config, "");
					bind(Config.class).toInstance(config);
					bind(Config.class).annotatedWith(Names.named("/")).toInstance(config);
				}
				configBinder.applyBindings(binder(), config, fileName + "/");

			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
