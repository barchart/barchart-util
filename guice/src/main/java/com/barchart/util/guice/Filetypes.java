package com.barchart.util.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

/*
 * TODO: Refactor this to be more sensible
 */
public final class Filetypes {

	private static final Logger logger = LoggerFactory.getLogger(Filetypes.class);

	static final String CONFIG_FILE_EXTENSION = ".conf";

	static final String COMPONENT_FILE_EXTENSION = ".component";

	static final String CONFIG_LIST = "component";

	public static final String DEFAULT_CONFIG_FILE = "application.conf";

	public static final String VALUE_CONVERTERS = "value_converters";

	public static final String MODULES = "module";

	public static final String MODULE_TYPE = "type";

	public static boolean isConfig(final Config config) {
		return isConfig(getOriginName(config));
	}

	public static boolean isConfig(final String name) {
		return name.endsWith(CONFIG_FILE_EXTENSION);
	}


	public static boolean isDefaultConfigFile(final Config config) {
		return getOriginName(config).endsWith("/" + DEFAULT_CONFIG_FILE);
	}

	public static boolean isComponentFile(final Config config) {
		return getOriginName(config).endsWith(COMPONENT_FILE_EXTENSION);
	}

	static String getOriginName(final Config config) {
		final String description = config.origin().description();
		return stripLinenumbersFromOriginDescription(description);
	}

	/*
	 * origin description sometimes has line numbers at the end
	 */
	static String stripLinenumbersFromOriginDescription(final String originDescription) {
		return originDescription.replaceAll(": \\d+-?\\d*$", "");
	}

	// no path, no extension
	public static String getSimpleName(final Config config) {
		logger.info("GetSimpleName: " +config + ", origin: " + config.origin() + ", description: " + config.origin().description());
		if (config.origin() == null) {
			return "";
		} else {
			return getSimpleName(config.origin().description());
		}

	}

	public static String getSimpleName(final String originDescription) {
		final String originName = stripLinenumbersFromOriginDescription(originDescription);
		final int lastIndexOf = originName.lastIndexOf("/");
		if (lastIndexOf == -1) {
			return "";
		}
		return originName.substring(lastIndexOf + 1, originName.lastIndexOf("."));
	}


}
