package com.barchart.util.guice.simple;

import javax.inject.Provider;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;

@Singleton
public class SystemConfigProvider implements Provider<Config> {

	private static final Logger logger = LoggerFactory.getLogger(SystemConfigProvider.class);
	
	private final Config config;

	SystemConfigProvider() {
		String configString = System.getProperty("config");
		this.config = ConfigFactory.parseString(configString);
		// Fallbacks?
		logger.info("Loaded system configuration from \"config\" system property.:\n" + config.root().render(ConfigRenderOptions.defaults().setComments(false).setOriginComments(false).setFormatted(true).setJson(false)));
	}
	
	@Override
	public Config get() {
		return config;
	}

}
