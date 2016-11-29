package com.barchart.util.guice;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

public final class ClassPathResources implements ConfigResources {

	private static final Logger logger = LoggerFactory.getLogger(ClassPathResources.class);

	private static final String CONFIG_RESOURCE_PATH = "META-INF/conf/";

	private Map<String, URL> map;

	private String pathDescription;
	private final String basePath;

	public ClassPathResources() throws IOException {
		this(CONFIG_RESOURCE_PATH);
	}

	public ClassPathResources(final String base) throws IOException {
		this.basePath = base;
		try {
			this.pathDescription = Resources.getResource(basePath).getFile();
			this.map = loadConfigResources();
		} catch (final IllegalArgumentException iae) {
			this.pathDescription = null;
			this.map = new HashMap<String, URL>();
		}
	}

	private Map<String, URL> loadConfigResources() throws IOException {
		final Map<String, URL> map = new HashMap<String, URL>();
		final ClassPath classPath = ClassPath.from(ClassPathResources.class.getClassLoader());
		for (final ResourceInfo info : classPath.getResources()) {
			try {
				if (info.url().getFile().startsWith(pathDescription)) {
					map.put(shorten(info.getResourceName()), info.url());
				}
			} catch (NullPointerException e) {
				// These NPEs started showing up when using Java 8 for
				// "source_tips" and "version.rc"
				// Should be safe to ignore
				logger.debug(e.getMessage());
			}
		}
		return map;
	}

	private String shorten(final String resourceName) {
		return resourceName.replace(basePath, "");
	}

	@Override
	public String readResource(final String resourceName) throws Exception {
		logger.info("Reading classpath resource: " + resourceName);
		return Resources.toString(getURL(resourceName), StandardCharsets.UTF_8);
	}

	@Override
	public Config readConfig(final String resourceName) throws Exception {
		try {
			Config rawConfig = ConfigFactory.parseURL(getURL(resourceName));
			Config withSystemProperties = ConfigFactory.systemProperties().withFallback(rawConfig);
			return withSystemProperties.resolve();
		} catch (ConfigException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private URL getURL(final String resourceName) {
		final URL url = map.get(resourceName);
		if (url == null) {
			throw new IllegalArgumentException("No resource with name:" + resourceName);
		}
		return url;
	}

	@Override
	public List<String> listResources() {
		return new ArrayList<String>(map.keySet());
	}

	@Override
	public String getPathDescription() {
		return pathDescription;
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

	@Override
	public String toString() {
		return "ClassPathResources: path: " + getPathDescription();
	}
}
