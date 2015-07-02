package com.barchart.util.guice;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.inject.Injector;

public final class GuiceLauncher {

	private static final Logger logger = LoggerFactory.getLogger(GuiceLauncher.class);

	public static <T> Injector buildInjector(final String... args) throws Exception {

		logCurrentDirectory();
		logPaths();
		logEnvironment();

		final GuiceConfigBuilder builder = GuiceConfigBuilder.create();

		final ArgParser parser = new ArgParser().parse(args);

		if (parser.has("conf")) {

			final String directory = parser.option("conf");

			if (directory != null) {
				if (!new File(directory).exists()) {
					logger.warn("Specified configuration directory does not exist: {}", directory);
				} else {
					logger.info("Using configuration directory: {}", directory);
					builder.setDirectory(directory, true);
				}
			}

		}

		return builder.build();
	}

	public static <T extends RunnableArgs> void run(final Class<T> clazz, final String... args) throws Exception {
		configure(clazz, args).run(args);
	}

	public static <T> T configure(final Class<T> clazz, final String... args) throws Exception {
		logger.info("Starting Guice Launcher.  Configuring: " + clazz);
		return buildInjector(args).getInstance(clazz);
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
