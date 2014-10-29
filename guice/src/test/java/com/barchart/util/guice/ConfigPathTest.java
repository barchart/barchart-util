package com.barchart.util.guice;

import static org.junit.Assert.*;
import java.io.File;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.Config;

public class ConfigPathTest {

	private static final class FullApplicationConf extends TestCase {

		@Inject
		@Named("application.conf/")
		private Config config;

		@Override
		void test() {
			assertEquals("application", config.getString("configName"));
		}
	}

	@Test
	public void testFullApplicationConf() {
		get(FullApplicationConf.class).test();
	}

	private Injector injector;

	@Before
	public void setup() {
		ConfigModule configModule = new ConfigModule(new File("src/test/resources/configpathtest"));
		this.injector = Guice.createInjector(configModule);
	}

	private <T> T get(Class<T> clazz) {
		return injector.getInstance(clazz);
	}
}
