package com.barchart.util.guice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.typesafe.config.Config;

public class MergedResources implements ConfigResources {

	private final ConfigResources[] resources;
	private StringResources merged = null;

	/**
	 * Create a new merged config provider that returns all resources for the provided set. Earlier resources take
	 * precedence over later ones in the argument list.
	 */
	public MergedResources(final ConfigResources... resources_) {
		resources = resources_;
	}

	@Override
	public String readResource(final String resourceName) throws Exception {
		return merged().readResource(resourceName);
	}

	@Override
	public Config readConfig(final String resourceName) throws Exception {
		return merged().readConfig(resourceName);
	}

	@Override
	public List<String> listResources() throws Exception {
		return merged().listResources();
	}

	@Override
	public String getPathDescription() throws Exception {

		final String[] names = new String[resources.length];

		for (int i = 0; i < resources.length; i++) {
			names[i] = resources[i].getPathDescription();
		}

		return "[" + Joiner.on(", ").join(names) + "]";

	}

	@Override
	public List<Config> readAllConfigs(final String fileExtension) throws Exception {
		return merged().readAllConfigs(fileExtension);
	}

	// Lazy load resources in order to throw exceptions at the correct place
	private StringResources merged() throws Exception {

		if (merged == null) {

			final Map<String, String> map = new HashMap<String, String>();

			// Reverse order so higher priority resources overwrite values
			for (int i = resources.length - 1; i >= 0; i--) {
				for (final String r : resources[i].listResources()) {
					map.put(r, resources[i].readResource(r));
				}
			}

			merged = new StringResources(map);

		}

		return merged;

	}

}
