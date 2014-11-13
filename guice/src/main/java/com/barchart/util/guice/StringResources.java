package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

public class StringResources implements ConfigResources {

	private final Map<String, String> resources;

	public StringResources(final String source) {
		resources = new HashMap<String, String>();
		resources.put("application.conf", source);
	}

	public StringResources(final Map<String, String> source) {
		resources = source;
	}

	@Override
	public String readResource(final String resourceName) throws Exception {
		return resources.get(resourceName);
	}

	@Override
	public Config readConfig(final String resourceName) throws Exception {

		final String content = readResource(resourceName);

		if (content != null) {
			return ConfigFactory.parseString(content, ConfigParseOptions.defaults().setOriginDescription(resourceName));
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

		for (final String resource : listResources()) {
			if (resource.endsWith(fileExtension)) {
				list.add(readConfig(resource));
			}
		}

		return list;

	}

}
