package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

/**
 * Holds key(filename) to file contents (as a String). Creates HOCON objects
 * from the content.
 *
 */
public class StringResources implements ConfigResources {

	private final Map<String, String> resources;

	public StringResources() {
		resources = new HashMap<String, String>();
	}

	public StringResources(final String source) {
		resources = new HashMap<String, String>();
		resources.put(Filetypes.DEFAULT_CONFIG_FILE, source);
	}

	public StringResources(final Map<String, String> source) {
		resources = source;
	}

	@Override
	public String readResource(final String resourceName) throws Exception {

		if (!resources.containsKey(resourceName)) {
			throw new IllegalArgumentException("Invalid resource");
		}

		return resources.get(resourceName);

	}

	@Override
	public Config readConfig(final String resourceName) throws Exception {

		final String content = readResource(resourceName);

		if (content != null) {
			return ConfigFactory.parseString(content,
					ConfigParseOptions.defaults().setOriginDescription("/" + resourceName)).resolve();
		}

		return ConfigFactory.empty();

	}

	@Override
	public List<String> listResources() throws Exception {
		return new ArrayList<String>(resources.keySet());
	}

	@Override
	public String getPathDescription() throws Exception {
		return "string input";
	}

	@Override
	public List<Config> readAllConfigs(final String fileExtension) throws Exception {

		final List<Config> list = new ArrayList<Config>();
		Config appDefaultConfig = null;

		List<String> resources = listResources();
		for (final String resource : resources) {
			if (resource.endsWith(fileExtension)) {
				Config c = readConfig(resource);
				if (resource.equals(Filetypes.DEFAULT_APPLICATION_CONFIG_FILE)) {
					/*
					 * This is the reference/default application configuration
					 * which is normally in the jar. We save here to allow
					 * overriding on the main configuration file
					 * (application.conf)
					 */
					appDefaultConfig = c;
				} else if (resource.equals(Filetypes.DEFAULT_CONFIG_FILE) && appDefaultConfig != null) {
					/*
					 * The HOCON library uses immutable objects so, must use the
					 * returned object.
					 * 
					 * NOTE: The origin name is now changed to include the
					 * merged file name.
					 */
					Config configWithFallBack = c.withFallback(appDefaultConfig);
					list.add(configWithFallBack);
				} else {
					list.add(c);
				}
			}
		}

		return list;

	}
}
