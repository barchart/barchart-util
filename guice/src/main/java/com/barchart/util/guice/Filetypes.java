package com.barchart.util.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

/*
 * TODO: Refactor this to be more sensible
 */
final class Filetypes {
	private static final Logger logger = LoggerFactory.getLogger(Filetypes.class);

	static final String CONFIG_FILE_EXTENSION = ".conf";

	static final String COMPONENT_FILE_EXTENSION = ".component";

	static final String DEFAULT_CONFIG_FILE = "application.conf";

	static final String CONFIG_LIST = "component";

	public static final String VALUE_CONVERTERS = "value_converters";

	public static final String MODULES = "module";

	public static final String MODULE_TYPE = "type";

	static boolean isConfig(Config config) {
		return isConfig(getOriginName(config));
	}
	
	static boolean isConfig(String name) {
		return name.endsWith(CONFIG_FILE_EXTENSION);
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
		logger.info("GetSimpleName: " +config + ", origin: " + config.origin() + ", description: " + config.origin().description());
		if (config.origin() == null) {
			return "";
		} else {
			return getSimpleName(config.origin().description());
		}

	}

	static String getSimpleName(String originDescription) {
		String originName = stripLinenumbersFromOriginDescription(originDescription);
		int lastIndexOf = originName.lastIndexOf("/");
		if (lastIndexOf == -1) {
			return "";
		}
		return originName.substring(lastIndexOf + 1, originName.lastIndexOf("."));
	}


}
