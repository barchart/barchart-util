package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;
import com.typesafe.config.Config;

public class ConfigPathTest {

	private static final String CONFIGURATION_DIRECTORY = "src/test/resources/configpathtest";

	private static final class TestConfigDirectory extends TestCase {

		@Inject
		private ConfigDirectory configDirectory;

		@Override
		void test() {
			assertEquals(CONFIGURATION_DIRECTORY, configDirectory.getFile());
		}

	}

	private static final class ApplicationConf extends TestCase {

		@Inject
		private Config configNoName;

		@Inject
		@Named("/")
		private Config configSlash;

		@Inject
		@Named("application/")
		private Config configApplication;

		@Override
		void test() {
			assertEquals("application", configNoName.getString("configName"));
			assertEquals(configNoName, configSlash);
			assertEquals(configNoName, configApplication);
		}

	}

	private static final class Config1 extends TestCase {

		@Inject
		@Named("config1/")
		private Config config;

		@Override
		void test() {
			assertEquals("config1", config.getString("configName"));
		}

	}

	private static final class Config2 extends TestCase {

		@Inject
		@Named("config2/")
		private Config config;

		@Inject
		@Named("config2/container1.container2.configName")
		private String container2ConfigName;

		@Override
		void test() {
			assertEquals("config2", config.getString("configName"));
			assertEquals("container2", container2ConfigName);
		}

	}

	@Test
	public void testConfigDirectory() {
		get(TestConfigDirectory.class);
	}

	@Test
	public void testAplicationConf() {
		get(ApplicationConf.class).test();
	}

	@Test
	public void testConfig1() {
		get(Config1.class).test();
	}

	@Test
	public void testConfig2() {
		get(Config2.class).test();
	}

	private Injector injector;

	@Before
	public void setup() {
		this.injector = GuiceConfigBuilder.create() //
				.setDirectory(CONFIGURATION_DIRECTORY) //
				.build();
	}

	private <T> T get(Class<T> clazz) {
		return injector.getInstance(clazz);
	}
}
