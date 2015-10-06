package com.barchart.util.guice;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.io.Resources;
import com.google.inject.Injector;

public final class GuiceLauncher {

	private static final Logger logger = LoggerFactory.getLogger(GuiceLauncher.class);
	private static final String GUICE_APP_MAIN_CLASS = "Guice-App-Main-Class";

	public static <T> Injector buildInjector(final String... args) throws Exception {

		logBuildInfo();
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

	private static void logBuildInfo() {
		try {
			final URL resource = Resources.getResource("META-INF/MANIFEST.MF");
			Manifest manifest;
			manifest = new Manifest(resource.openStream());
			final Attributes mainAttributes = manifest.getMainAttributes();
			final String guiceAppMainClass = mainAttributes.getValue(GUICE_APP_MAIN_CLASS);
			final String artifact = mainAttributes.getValue("Maven-Artifact");
			final String version = mainAttributes.getValue("Implementation-Version");
			final String buildDate = mainAttributes.getValue("Build-Date");

			StringBuilder sb = new StringBuilder("Build: ");
			sb.append("\n\tGuice Main Class: " + guiceAppMainClass);
			sb.append("\n\tArtifact: " + artifact);
			sb.append("\n\tVersion: " + version);
			sb.append("\n\tBuild Date: " + buildDate);

			logger.info(sb.toString());

		} catch (IOException ignore) {
		}

	}

	public static <T> void run(final Class<T> clazz, final String... args) throws Exception {
		T instance = configure(clazz, args);
		if (instance instanceof RunnableArgs) {
			((RunnableArgs) instance).run(args);
		} else if (instance instanceof Runnable) {
			((Runnable) instance).run();
		} else {
			throw new IllegalStateException("Could not run type " + clazz);
		}
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
