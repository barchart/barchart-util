package com.barchart.util.guice;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Resources;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ResourceInfo;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class ClassPathResources implements ConfigResources {

	private static final String CONFIG_RESOURCE_PATH = "META-INF/conf";

	private final Map<String, URL> map;

	private final String pathDescription;

	public ClassPathResources() throws IOException {
		this.pathDescription = Resources.getResource(CONFIG_RESOURCE_PATH).getFile();
		this.map = loadConfigResources();
	}

	private Map<String, URL> loadConfigResources() throws IOException {
		Map<String, URL> map = new HashMap<String, URL>();
		ClassPath classPath = ClassPath.from(ClassPathResources.class.getClassLoader());
		for (ResourceInfo info : classPath.getResources()) {
			if (info.url().getFile().startsWith(pathDescription)) {
				map.put(info.getResourceName(), info.url());
			}
		}
		return map;
	}

	@Override
	public String readResource(String resourceName) throws Exception {
		return Resources.toString(getURL(resourceName), StandardCharsets.UTF_8);
	}

	@Override
	public Config readConfig(String resourceName) throws Exception {
		return ConfigFactory.parseURL(getURL(resourceName));
	}

	private URL getURL(String resourceName) {
		URL url = map.get(resourceName);
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

}
