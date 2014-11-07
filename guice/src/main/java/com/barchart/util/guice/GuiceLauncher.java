package com.barchart.util.guice;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.inject.Injector;

public final class GuiceLauncher {

	private static final Logger logger = LoggerFactory.getLogger(GuiceLauncher.class);

	public static <T> T configure(Class<T> clazz) throws Exception {
		logger.info("Starting Guice Launcher.  Configuring: " + clazz);
		logCurrentDirecotry();
		logPaths();
		logEnvironment();
		Injector injector = GuiceConfigBuilder.create() //
				.build();
		return injector.getInstance(clazz);
	}

	private static void logCurrentDirecotry() {
		logger.info("user.dir = " + System.getProperty("user.dir"));

	}

	private static void logPaths() {
		StringBuilder builder = new StringBuilder();
		builder.append(getPaths("sun.boot.class.path"));
		builder.append(getPaths("sun.boot.library.path"));
		builder.append(getPaths("java.library.path"));
		builder.append(getPaths("java.class.path"));
		logger.info("Paths:" + builder.toString());
	}

	private static String getPaths(final String property) {
		String paths = System.getProperty(property);
		String[] pathArray = paths.split(File.pathSeparator);
		return "\n\t" + property + "\n\t\t" + Joiner.on("\n\t\t").join(pathArray);
	}

	private static void logEnvironment() {

	}

}
