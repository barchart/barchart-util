package com.barchart.util.guice;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

public final class GuiceMain {

	private static final Logger logger = LoggerFactory.getLogger(GuiceMain.class);

	private final GuiceApp guiceApp;

	// TODO: Shouldn't need to name components if only one exists
	@Inject
	public GuiceMain(@Named("GuiceApp") GuiceApp guiceApp) {
		this.guiceApp = guiceApp;
	}

	private void runApp(String[] args) {
		try {
			guiceApp.run(args);
		} catch (Exception e) {
			logger.error("Fatal exception in application.", e);
		}
	}

	public static void main(String[] args) throws Exception {
		Injector injector = GuiceConfigBuilder.create() //
				.build();
		GuiceMain guiceMain = injector.getInstance(GuiceMain.class);
		guiceMain.runApp(args);
	}

}
