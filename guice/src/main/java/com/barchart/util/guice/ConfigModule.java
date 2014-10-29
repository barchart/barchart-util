package com.barchart.util.guice;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class ConfigModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigModule.class);

	private static final String APPLICATION_CONF = "application.conf";

	private static final String COMPONENT_LIST = "component";

	private static final String COMPONENT_TYPE = "type";

	private final File configDirectory;

	private final String applicationConf;

	public ConfigModule(File configDirectory) {
		this(configDirectory, APPLICATION_CONF);

	}

	public ConfigModule(File configDirectory, String applicationConf) {
		if (!configDirectory.isDirectory()) {
			throw new IllegalArgumentException(configDirectory + " is not a directory");
		}
		this.configDirectory = configDirectory;
		this.applicationConf = applicationConf;
	}

	@Override
	protected void configure() {
		bind(ConfigDirectory.class).toInstance(new ConfigDirectory(configDirectory));
		bind(Config.class).toInstance(getApplicationConf());
		Map<String, Config> map = findComponentConfigList();
		for (String type : map.keySet()) {
			bind(Config.class).annotatedWith(Names.named(type)).toInstance(map.get(type));
		}

	}

	private Map<String, Config> findComponentConfigList() {
		Map<String, Config> map = new HashMap<String, Config>();
		Config applicationConf = getApplicationConf();
		if (applicationConf != null) {
			for (Config componentConf : applicationConf.getConfigList(COMPONENT_LIST)) {
				String type = componentConf.getString(COMPONENT_TYPE);
				if (type != null) {
					map.put(type, componentConf);
				}
			}
		}
		return map;
	}

	private Config getApplicationConf() {
		File applicationConfFile = new File(configDirectory, applicationConf);
		if (applicationConfFile.exists()) {
			return ConfigFactory.parseFile(applicationConfFile);
		} else {
			return null;
		}
	}

}
