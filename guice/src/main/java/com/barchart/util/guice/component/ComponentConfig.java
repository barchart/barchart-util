package com.barchart.util.guice.component;

import com.google.inject.Module;
import com.typesafe.config.Config;

class ComponentConfig {

	private static final String TYPE = "type";

	private static final String NAME = "name";
	
	private final Config config;

	public ComponentConfig(Config c) {
		this.config = c;
	}

	public String getType() {
		return config.getString(TYPE);
		
	}

	public String getName() {
		return config.getString(NAME);
	}

	public Config getConfig() {
		return config;
	}

	public Module createModule() {
//		return new AbstractModule() {
		return null;
	}

}
