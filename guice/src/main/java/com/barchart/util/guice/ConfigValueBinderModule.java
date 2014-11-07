package com.barchart.util.guice;

import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

final class ConfigValueBinderModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigValueBinderModule.class);

	@Inject
	private ConfigResources resources;

	@Inject
	private Set<ValueConverter> valueConverters;

	private BindUtil bindUtil;

	ConfigValueBinderModule() {
	}

	@Override
	protected void configure() {
		// FIXME: ? hack to get correct injector in ModuleLoaderModule
		bind(ModuleLoaderModule.class);
		try {
			this.bindUtil = new BindUtil(super.binder());
			for (Config config : resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
				logger.info("Binding values from config file: " + config.origin().description());
				ConfigFileBinder configFileBinder = new ConfigFileBinder(config);
				configFileBinder.applyBindings();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final class ConfigFileBinder {

		private final Config config;

		private final String simpleFilename;

		public ConfigFileBinder(Config config) {
			this.config = config;
			this.simpleFilename = Filetypes.getSimpleName(config);
		}

		public void applyBindings() {

			if (Filetypes.isDefaultConfigFile(config)) {
				bind(Config.class).toInstance(config);
				bind(Config.class).annotatedWith(Names.named("/")).toInstance(config);
			}

			UniqueObjectPathSet objectPaths = new UniqueObjectPathSet();
			for (Entry<String, ConfigValue> entry : config.entrySet()) {
				String configValuePath = entry.getKey();
				objectPaths.add(configValuePath);
				bindConfigValue(configValuePath, entry.getValue());
			}
			bindConfigObjectPaths(objectPaths);
		}

		private void bindConfigValue(String key, ConfigValue value) {
			for (ValueConverter converter : valueConverters) {
				Object result = converter.convert(value);
				if (result != null) {
					bindUtil.bindInstance(converter.getBindingType(), getPath(key), result);
					if (Filetypes.isDefaultConfigFile(config)) {
						bindUtil.bindInstance(converter.getBindingType(), key, result);
					}
				}
			}
		}

		private void bindConfigObjectPaths(UniqueObjectPathSet objectPaths) {
			bindConfigValue("", config.root());

			for (String objectPath : objectPaths) {
				Config object = config.getConfig(objectPath);
				bindConfigValue(objectPath, object.root());
			}
		}

		private String getPath(String key) {
			return simpleFilename + "/" + key;
		}
	}

}
