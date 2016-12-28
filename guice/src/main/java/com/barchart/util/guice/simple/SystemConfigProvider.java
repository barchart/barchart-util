package com.barchart.util.guice.simple;

import javax.inject.Provider;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SystemConfigProvider implements Provider<Config> {

	private final Config config;

	SystemConfigProvider() {
		String configString = System.getProperty("config");
		this.config = ConfigFactory.parseString(configString);
		// Fallbacks?
	}
	
	@Override
	public Config get() {
		return config;
	}

}
