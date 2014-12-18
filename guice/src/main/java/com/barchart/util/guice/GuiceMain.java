package com.barchart.util.guice;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.TimeoutException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

public class GuiceMain {

	private static final Logger logger = LoggerFactory.getLogger(GuiceMain.class);

	private static final String TESTING_OPTION = "test";

	private static final String GUICE_APP_MAIN_CLASS = "Guice-App-Main-Class";

	private static final long WIRING_TEST_TIMEOUT = 10000;

	public static void main(final String[] args) throws Exception {

		logger.info("Inspecting manifest for GuiceApp class");

		final URL resource = Resources.getResource("META-INF/MANIFEST.MF");
		final Manifest manifest = new Manifest(resource.openStream());
		final Attributes mainAttributes = manifest.getMainAttributes();
		final String guiceAppMainClass = mainAttributes.getValue(GUICE_APP_MAIN_CLASS);

		logger.info(GUICE_APP_MAIN_CLASS + ": " + guiceAppMainClass);

		final Class<?> mainClass = loadMainClass(guiceAppMainClass);

		final ArgParser parser = new ArgParser().parse(args);

		if (parser.has("test")) {
			logger.info("Starting wiring test.");
			runTest(mainClass, args);
		} else {
			logger.info("Starting application.");
			runMain(mainClass, args);
		}

	}

	private static Class<?> loadMainClass(final String guiceAppMainClass) throws Exception {
		try {
			return GuiceMain.class.getClassLoader().loadClass(guiceAppMainClass);
		} catch (final Exception e) {
			logger.error("Could not load main class: " + guiceAppMainClass + ". Make sure " + GUICE_APP_MAIN_CLASS
					+ "   attribute is set correctly in the manifest.");
			throw e;
		}
	}

	private static void runMain(final Class<?> mainClass, final String[] args) throws Exception {
		final Method m = mainClass.getMethod("main", String[].class);
		m.invoke(null, (Object) args);
	}

	private static void runTest(final Class<?> mainClass, final String[] args) throws Exception {
		final WiringTestRunner thread = new WiringTestRunner(mainClass, args);
		thread.start();
		thread.join(WIRING_TEST_TIMEOUT);
		final Exception exception = thread.exception;
		if (exception != null) {
			throw new RuntimeException("Wiring test failed", exception);
		}
		if (thread.isAlive()) {
			logger.info("Interrupting wiring test.");
			thread.interrupt();
			throw new TimeoutException("Wiring test did not complete in " + WIRING_TEST_TIMEOUT + " milliseconds");
		}
	}

	private static final class WiringTestRunner extends Thread {

		private final Class<?> mainClass;

		private final String[] args;

		private volatile Exception exception;

		public WiringTestRunner(final Class<?> mainClass, final String[] args) {
			super("WiringTest");
			this.mainClass = mainClass;
			this.args = args;
		}

		@Override
		public void run() {
			try {
				final Method m = mainClass.getMethod("test", String[].class);
				m.invoke(null, (Object) args);
			} catch (final Exception e) {
				this.exception = e;
			}
		}

	}

}
