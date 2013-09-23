package com.barchart.util.hocon;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class NestedConfigsTest {

	private static final Logger logger = LoggerFactory.getLogger(BasicTypesTest.class);

	public static final String CONFIG_FILE = "src/test/resources/nested_configs.conf";

	public interface OuterConfig {

		public InnerInterface getInnerConfig1();

		public InnerInterface getInnerConfig2();

		public List<InnerInterface> getInnerConfigList();

	}

	public interface InnerInterface {
		public String getName();
	}

	private static Config rawConfig;

	private static OuterConfig proxyConfig;

	@BeforeClass
	public static void setup() {
		HoconProxyLoader loader = new HoconProxyLoader();
		rawConfig = ConfigFactory.parseFile(new File(CONFIG_FILE));
		proxyConfig = loader.loadProxy(OuterConfig.class, new File(CONFIG_FILE));
	}

	@Test
	public void testInnerConfig1() {
		Config rawInnerConfig = rawConfig.getConfig("inner_config1");
		assertEquals(rawInnerConfig.getString("name"), proxyConfig.getInnerConfig1().getName());
	}
	
	@Test
	public void testInnerConfig2() {
		Config rawInnerConfig = rawConfig.getConfig("inner_config2");
		assertEquals(rawInnerConfig.getString("name"), proxyConfig.getInnerConfig2().getName());
	}	
	
	@Test
	public void testInnerConfigList() {
		List<? extends Config> rawInnerConfigList = rawConfig.getConfigList("inner_config_list");
		List<InnerInterface> proxyInnerConfigList = proxyConfig.getInnerConfigList();
		assertEquals(rawInnerConfigList.size(), proxyInnerConfigList.size());
		
		int index = 0;
		for (Config rawConfig : rawInnerConfigList) {
			assertEquals(rawConfig.getString("name"), proxyInnerConfigList.get(index).getName());
			index++;
		}
	}

}
