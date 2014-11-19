package com.barchart.util.guice;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

final class ConfigValueBinderModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigValueBinderModule.class);

	@Inject
	private ConfigResources resources;

	@Inject
	private ValueConverterTool valueConverterTool;


	ConfigValueBinderModule() {
	}

	@Override
	protected void configure() {
		// FIXME: ? hack to get correct injector in ModuleLoaderModule
		bind(ModuleLoaderModule.class);
		try {
			for (Config config : resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
				logger.info("Binding values from config file: " + config.origin().description());
				ConfigBinder configFileBinder = new ConfigBinder(config, valueConverterTool);
				configFileBinder.applyBindings(binder());
				
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}



}
