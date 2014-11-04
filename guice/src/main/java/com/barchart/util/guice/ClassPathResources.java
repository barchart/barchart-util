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
import com.typesafe.config.ConfigFactory;

public final class ClassPathResources implements ConfigResources {

	private static final Logger logger = LoggerFactory.getLogger(ClassPathResources.class);
	
	private static final String CONFIG_RESOURCE_PATH = "META-INF/conf/";

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
				map.put(shorten(info.getResourceName()), info.url());
			}
		}
		return map;
	}

	private String shorten(String resourceName) {
		return resourceName.replace(CONFIG_RESOURCE_PATH, "");
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

	@Override
	public List<Config> readAllConfigs(String fileExtension) throws Exception {
		List<Config> list = new ArrayList<Config>();
		for (String resource : listResources()) {
			if (resource.endsWith(fileExtension)) {
				list.add(readConfig(resource));
			}
		}
		return list;
	}

}
