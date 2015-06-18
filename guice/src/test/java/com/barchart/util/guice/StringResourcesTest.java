package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.typesafe.config.Config;

public class StringResourcesTest {

	private StringResources resources;
	private Map<String, String> resourceFilesMap;
	private String appConfig;
	private String appDefaultConfig;
	private String otherConfig;

	@Before
	public void setup() {
		appConfig = getApplicationConfString();
		appDefaultConfig = getApplicationConfString();
		otherConfig = getApplicationConfString();
		resourceFilesMap = new HashMap<String, String>();
	}

	@Test
	public void readResource() throws Exception {
		resourceFilesMap.put(Filetypes.DEFAULT_CONFIG_FILE, appConfig);
		resources = new StringResources(resourceFilesMap);
		String content = resources.readResource(Filetypes.DEFAULT_CONFIG_FILE);
		assertNotNull(content);
	}

	@Test(expected = IllegalArgumentException.class)
	public void readResourceNotFound() throws Exception {
		resourceFilesMap.put(Filetypes.DEFAULT_CONFIG_FILE, appConfig);
		resources = new StringResources(resourceFilesMap);
		String content = resources.readResource("bad file");
	}

	@Test
	public void readAllConfigsOnlyDefaultFile() throws Exception {
		resourceFilesMap.put(Filetypes.DEFAULT_CONFIG_FILE, appConfig);
		resources = new StringResources(resourceFilesMap);
		List<Config> configs = resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION);
		assertEquals(1, configs.size());
		Config config = configs.get(0);
		assertEquals("/application.conf: 1", config.origin().description());

	}

	@Test
	public void readAllConfigsOnlyWithDefaultApplicationFile() throws Exception {
		resourceFilesMap.put(Filetypes.DEFAULT_CONFIG_FILE, appConfig);
		resourceFilesMap.put(Filetypes.DEFAULT_APPLICATION_CONFIG_FILE, appDefaultConfig);
		resourceFilesMap.put("otherConfigFile" + Filetypes.CONFIG_FILE_EXTENSION, otherConfig);
		resources = new StringResources(resourceFilesMap);
		List<Config> configs = resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION);
		assertEquals(2, configs.size());
		Config config = configs.get(0);
		assertEquals("/otherConfigFile.conf: 1", config.origin().description());
		config = configs.get(1);
		assertEquals("/application.conf: 1", config.origin().description());

	}

	private String getApplicationConfString() {
		StringBuilder b = new StringBuilder();
		b.append("component = [\n");
		b.append("  {\n");
		b.append("    type = \"ComponentGenericBinding.MyComponent\"\n");
		b.append("  }\n");
		b.append("]");
		return b.toString();
	}

}
