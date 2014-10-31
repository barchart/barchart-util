package com.barchart.util.guice;

import com.typesafe.config.Config;

public final class Filetypes {

	private static final String CONFIG_FILE_EXTENSION = ".conf";
	
	private static final String DEFAULT_CONFIG_FILE = "application.conf";

	public static boolean isConfigFile(Config config) {
		return config.origin().filename().endsWith(CONFIG_FILE_EXTENSION);
	}

	public static boolean isDefaultConfigFile(Config config) {
		return config.origin().filename().endsWith("/" + DEFAULT_CONFIG_FILE);
	}

}
