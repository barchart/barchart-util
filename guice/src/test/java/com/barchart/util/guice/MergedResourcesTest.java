package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MergedResourcesTest {

	private StringResources stringResources;
	private MergedResources mergedResources;
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
	public void merged() throws Exception {
		resourceFilesMap.put(Filetypes.DEFAULT_APPLICATION_CONFIG_FILE, appDefaultConfig);
		resourceFilesMap.put(Filetypes.DEFAULT_CONFIG_FILE, appConfig);
		resourceFilesMap.put("otherFile.conf", otherConfig);
		stringResources = new StringResources(resourceFilesMap);
		mergedResources = new MergedResources(stringResources);
		StringResources finalMergedResources = mergedResources.merged();
		assertNotNull(finalMergedResources);
		List<String> l = finalMergedResources.listResources();
		assertEquals("otherFile.conf", l.get(0));
		assertEquals(Filetypes.DEFAULT_APPLICATION_CONFIG_FILE, l.get(1));
		assertEquals(Filetypes.DEFAULT_CONFIG_FILE, l.get(2));
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
