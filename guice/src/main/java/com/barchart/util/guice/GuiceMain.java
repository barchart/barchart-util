package com.barchart.util.guice;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

public class GuiceMain {

	private static final Logger logger = LoggerFactory.getLogger(GuiceMain.class);

	private static final String TESTING_OPTION = "test";

	private static final String GUICE_APP_MAIN_CLASS = "Guice-App-Main-Class";

	public static void main(String[] args) throws Exception {
		logger.info("Inspecting manifest for GuiceApp class");
		URL resource = Resources.getResource("META-INF/MANIFEST.MF");
		Manifest manifest = new Manifest(resource.openStream());
		Attributes mainAttributes = manifest.getMainAttributes();
		String guiceAppMainClass = mainAttributes.getValue(GUICE_APP_MAIN_CLASS);
		logger.info(GUICE_APP_MAIN_CLASS + ": " + guiceAppMainClass);
		Class<?> mainClass = GuiceMain.class.getClassLoader().loadClass(guiceAppMainClass);

		if (args.length > 0 && args[0].equals(TESTING_OPTION)) {
			runTest(mainClass, args);
		} else {
			runMain(mainClass, args);
		}
	}

	private static void runMain(Class<?> mainClass, String[] args) throws Exception {
		Method m = mainClass.getMethod("main", String[].class);
		m.invoke(null, (Object) args);
	}

	private static void runTest(Class<?> mainClass, String[] args) throws Exception {
		Method m = mainClass.getMethod("test", String[].class);
		m.invoke(null, (Object) args);
	}

}
