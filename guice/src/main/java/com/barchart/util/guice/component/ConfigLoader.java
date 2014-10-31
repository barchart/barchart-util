package com.barchart.util.guice.component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.barchart.util.guice.ConfigDirectory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

final class ConfigLoader {

	private static final String CONF_FILE_EXTENSION = "conf";

	private static final String COMPONENT_FILE_EXTENSION = "component";

	private static final String MODULE_FILE_EXTENSION = "module";

	private static final String COMPONENT_LIST = "component";

	private final ConfigDirectory dir;

	private final ArrayList<ConfigField> configFields;

	private final ArrayList<ComponentConfig> componentConfigs;

	public ConfigLoader(ConfigDirectory dir) {
		this.dir = dir;
		this.configFields = new ArrayList<ConfigField>();
		this.componentConfigs = new ArrayList<ComponentConfig>();
		load();
	}

	private void load() {
		loadConfigFiles();
		loadComponentFiles();
		loadModuleFiles();
	}

	private void loadConfigFiles() {
		for (File file : dir.listFiles(CONF_FILE_EXTENSION)) {

			UniqueObjectPathSet objectPathSet = new UniqueObjectPathSet();

			Config config = ConfigFactory.parseFile(file);
			for (Entry<String, ConfigValue> entry : config.entrySet()) {
				objectPathSet.add(entry.getKey());
				configFields.add(new ConfigField(file, entry.getKey(), entry.getValue()));
			}

			for (String objectPath : objectPathSet) {
				Config objectConfig = config.getConfig(objectPath);
				configFields.add(new ConfigField(file, objectPath, objectConfig.root()));
			}

			if (config.hasPath(COMPONENT_LIST)) {
				for (Config c : config.getConfigList(COMPONENT_LIST)) {
					componentConfigs.add(new ComponentConfig(c));
				}
			}
		}
	}

	private void loadComponentFiles() {
		for (File file : dir.listFiles(COMPONENT_FILE_EXTENSION)) {
			Config c = ConfigFactory.parseFile(file);
			componentConfigs.add(new ComponentConfig(c));
		}
	}

	private void loadModuleFiles() {
	}

	public List<ConfigField> getConfigFields() {
		return configFields;
	}

}
