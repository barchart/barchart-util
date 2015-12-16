package com.barchart.util.guice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.typesafe.config.Config;

public class MergedResources implements ConfigResources {

	private static final Logger logger = LoggerFactory.getLogger(MergedResources.class);

	private final ConfigResources[] resources;
	private StringResources merged = null;

	/**
	 * Create a new merged config provider that returns all resources for the
	 * provided set. Earlier resources take precedence over later ones in the
	 * argument list.
	 */
	public MergedResources(final ConfigResources... resources_) {
		resources = resources_;
	}

	@Override
	public String readResource(final String resourceName) throws Exception {
		logger.info("Reading merged resource: " + resourceName);
		return merged().readResource(resourceName);
	}

	@Override
	public Config readConfig(final String resourceName) throws Exception {

		return merged().readConfig(resourceName).resolve();
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
		StringResources stringResources = merged();
		return stringResources.readAllConfigs(fileExtension);
	}

	// Lazy load resources in order to throw exceptions at the correct place
	StringResources merged() throws Exception {

		if (merged == null) {

			final Map<String, String> map = new HashMap<String, String>();

			logger.info("Loading Configuration Resources: ");
			for (ConfigResources r : resources) {
				logger.info("\t" + r.toString());
			}
			/*
			 * Reverse order so higher priority resources overwrite values.
			 * 
			 * The overriding is based on the filename, so substitution of
			 * content is at the file level.
			 */
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
