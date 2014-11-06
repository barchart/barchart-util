package com.barchart.util.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

public final class GuiceLauncher {

	private static final Logger logger = LoggerFactory.getLogger(GuiceLauncher.class);

	public static <T> T configure(Class<T> clazz) throws Exception {
		// TODO: The reason this this class exists is
		// so we can do a bunch of useful logging on startup like
		// environment info, classpath, etc. Otherwise, the app
		// could just use the GuiceConfigBuilder directly.
		Injector injector = GuiceConfigBuilder.create() //
				.build();
		return injector.getInstance(clazz);
	}

}
