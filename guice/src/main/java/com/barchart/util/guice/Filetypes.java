package com.barchart.util.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

final class Filetypes {
	private static final Logger logger = LoggerFactory.getLogger(Filetypes.class);

	static final String CONFIG_FILE_EXTENSION = ".conf";

	static final String COMPONENT_FILE_EXTENSION = ".component";

	static final String DEFAULT_CONFIG_FILE = "application.conf";

	static final String CONFIG_LIST = "component";

	static boolean isConfigFile(Config config) {
		return getOriginName(config).endsWith(CONFIG_FILE_EXTENSION);
	}

	static boolean isDefaultConfigFile(Config config) {
		return getOriginName(config).endsWith("/" + DEFAULT_CONFIG_FILE);
	}

	static boolean isComponentFile(Config config) {
		return getOriginName(config).endsWith(COMPONENT_FILE_EXTENSION);
	}

	static String getOriginName(Config config) {
		String description = config.origin().description();
		return stripLinenumbersFromOriginDescription(description);
	}

	/*
	 * origin description sometimes has line numbers at the end
	 */
	static String stripLinenumbersFromOriginDescription(String originDescription) {
		return originDescription.replaceAll(": \\d+-?\\d*$", "");
	}

	// no path, no extension
	static String getSimpleName(Config config) {
		return getSimpleName(config.origin().description());

	}

	static String getSimpleName(String originDescription) {
		String originName = stripLinenumbersFromOriginDescription(originDescription);
		return originName.substring(originName.lastIndexOf("/") + 1, originName.lastIndexOf("."));
	}
}
