package com.barchart.util.guice;

import com.typesafe.config.Config;

final class Filetypes {

	static final String CONFIG_FILE_EXTENSION = ".conf";

	static final String COMPONENT_FILE_EXTENSION = ".component";

	static final String DEFAULT_CONFIG_FILE = "application.conf";

	static final String CONFIG_LIST = "component";

	static boolean isConfigFile(Config config) {
		return config.origin().filename().endsWith(CONFIG_FILE_EXTENSION);
	}

	static boolean isDefaultConfigFile(Config config) {
		return config.origin().filename().endsWith("/" + DEFAULT_CONFIG_FILE);
	}

	static boolean isComponentFile(Config config) {
		return config.origin().filename().endsWith(COMPONENT_FILE_EXTENSION);
	}

}
