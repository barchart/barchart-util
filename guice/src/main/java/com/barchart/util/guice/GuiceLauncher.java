package com.barchart.util.guice;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

public final class GuiceLauncher {

	private static final Logger logger = LoggerFactory.getLogger(GuiceLauncher.class);

	public static <T extends Runnable> void run(final Class<T> clazz) throws Exception {
		configure(clazz, null).run();
	}

	public static <T extends Runnable> void run(final Class<T> clazz, final String directory) throws Exception {
		configure(clazz, directory).run();
	}

	public static <T> T configure(final Class<T> clazz) throws Exception {
		return configure(clazz, null);
	}

	public static <T> T configure(final Class<T> clazz, final String directory) throws Exception {

		logger.info("Starting Guice Launcher.  Configuring: " + clazz);

		logCurrentDirectory();
		logPaths();
		logEnvironment();

		final GuiceConfigBuilder builder = GuiceConfigBuilder.create();

		if (directory != null)
			builder.setDirectory(directory, true);

		return builder.build().getInstance(clazz);

	}

	private static void logCurrentDirectory() {
		logger.info("user.dir = " + System.getProperty("user.dir"));

	}

	private static void logPaths() {
		final StringBuilder builder = new StringBuilder();
		builder.append(getPaths("sun.boot.class.path"));
		builder.append(getPaths("sun.boot.library.path"));
		builder.append(getPaths("java.library.path"));
		builder.append(getPaths("java.class.path"));
		logger.info("Paths:" + builder.toString());
	}

	private static String getPaths(final String property) {
		final String paths = System.getProperty(property);
		final String[] pathArray = paths.split(File.pathSeparator);
		return "\n\t" + property + "\n\t\t" + Joiner.on("\n\t\t").join(pathArray);
	}

	private static void logEnvironment() {

	}

}
