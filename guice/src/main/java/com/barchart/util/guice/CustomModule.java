package com.barchart.util.guice;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

public class CustomModule extends AbstractModule {

	protected final Config config;

	public CustomModule(Config config) {
		this.config = config;
	}
	@Override
	protected void configure() {
	}

}
