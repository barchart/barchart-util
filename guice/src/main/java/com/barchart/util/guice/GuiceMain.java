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

	private static final long WIRING_TEST_TIMEOUT = 5000;

	public static void main(String[] args) throws Exception {
		logger.info("Inspecting manifest for GuiceApp class");
		URL resource = Resources.getResource("META-INF/MANIFEST.MF");
		Manifest manifest = new Manifest(resource.openStream());
		Attributes mainAttributes = manifest.getMainAttributes();
		String guiceAppMainClass = mainAttributes.getValue(GUICE_APP_MAIN_CLASS);
		logger.info(GUICE_APP_MAIN_CLASS + ": " + guiceAppMainClass);
		Class<?> mainClass = GuiceMain.class.getClassLoader().loadClass(guiceAppMainClass);

		if (args.length > 0 && args[0].equals(TESTING_OPTION)) {
			logger.info("Starting wiring test.");
			runTest(mainClass, args);
		} else {
			logger.info("Starting application.");
			runMain(mainClass, args);
		}
		logger.info("GuiceApp completed normally.");
		System.exit(0);
	}

	private static void runMain(Class<?> mainClass, String[] args) throws Exception {
		Method m = mainClass.getMethod("main", String[].class);
		m.invoke(null, (Object) args);
	}

	private static void runTest(final Class<?> mainClass, final String[] args) throws Exception {
		WiringTestRunner thread = new WiringTestRunner(mainClass, args);
		thread.start();
		thread.join(WIRING_TEST_TIMEOUT);
		Exception exception = thread.exception;
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

		public WiringTestRunner(Class<?> mainClass, String[] args) {
			super("WiringTest");
			this.mainClass = mainClass;
			this.args = args;
		}

		@Override
		public void run() {
			try {
				Method m = mainClass.getMethod("test", String[].class);
				m.invoke(null, (Object) args);
			} catch (Exception e) {
				this.exception = e;
			}
		}

	}

}
