package com.barchart.util.guice;

import java.io.File;
import java.io.FilenameFilter;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class ConfigModule extends AbstractModule {

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
		bindMainConfigurationFile();
	}

	private void bindMainConfigurationFile() {
		File applicationConfFile = new File(configDirectory, applicationConf);
		if (applicationConfFile.exists()) {
			Config applicationConf = ConfigFactory.parseFile(applicationConfFile);
			bind(Config.class).toInstance(applicationConf);
			if (applicationConf.hasPath(COMPONENT_LIST)) {
				for (Config componentConf : applicationConf.getConfigList(COMPONENT_LIST)) {
					String type = componentConf.getString(COMPONENT_TYPE);
					if (type != null) {
						bind(Config.class).annotatedWith(Names.named(type)).toInstance(componentConf);
					}
				}
			}
		}
	}

}
