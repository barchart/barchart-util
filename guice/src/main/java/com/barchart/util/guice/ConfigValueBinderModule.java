package com.barchart.util.guice;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.component.UniqueObjectPathSet;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public final class ConfigValueBinderModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigValueBinderModule.class);
	
	private final List<Config> configFiles;
	
	private final List<ValueConverter> valueConverters;

	public ConfigValueBinderModule(List<Config> configFiles, List<ValueConverter> valueConverters) {
		this.configFiles = configFiles;
		this.valueConverters = valueConverters;
	}

	@Override
	protected void configure() {
		for (Config config : configFiles) {
			if (Filetypes.isConfigFile(config)) {
				ConfigFileBinder configFileBinder = new ConfigFileBinder(config);
				configFileBinder.applyBindings();
			}
		}
	}

	private final class ConfigFileBinder {

		private final Config config;

		private final String simpleFilename;

		public ConfigFileBinder(Config config) {
			this.config = config;
			this.simpleFilename = getSimpleName(config.origin().filename());
		}

		private String getSimpleName(String filename) {
			return filename.substring(filename.lastIndexOf("/") + 1, filename.lastIndexOf("."));
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
			applyConfigBindings(objectPaths);
		}

		private void applyConfigBindings(UniqueObjectPathSet objectPaths) {
			bindConfigValue("", config.root());
			
			for (String objectPath : objectPaths) {
				Config object = config.getConfig(objectPath);
				bindConfigValue(objectPath, object.root());
			}
		}

		private void bindConfigValue(String key, ConfigValue value) {
			for (ValueConverter converter : valueConverters) {
				converter.applyBindings(binder(), getPath(key), value);
				if (Filetypes.isDefaultConfigFile(config)) {
					converter.applyBindings(binder(), key, value);
				}
			}
		}

		private String getPath(String key) {
			return simpleFilename + "/" + key;
		}
	}

}
