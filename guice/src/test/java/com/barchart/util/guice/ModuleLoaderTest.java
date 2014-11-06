package com.barchart.util.guice;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.typesafe.config.Config;

public class ModuleLoaderTest {

	private static final Logger logger = LoggerFactory.getLogger(ModuleLoaderTest.class);

	private Injector injector;

	@Before
	public void setup() throws Exception {
		injector = GuiceConfigBuilder.create().setDirectory("src/test/resources/ModuleLoaderTest").build();
	}

	@Test
	public void autoinstalledModuleBinding() throws Exception {
		SomeInterface someInterface = injector.getInstance(SomeInterface.class);
		assertEquals(SomeInterfaceImpl.class, someInterface.getClass());
	}

	@Test
	public void moduleAccessesLocalConfigurationValue() throws Exception {
		assertEquals("bar", injector.getInstance(Key.get(String.class, Names.named("__module_config-foo"))));
	}

	@Test
	public void moduleAccessesLocalConfigObject() throws Exception {
		Config config = injector.getInstance(Key.get(Config.class, Names.named("__module_config-object")));
		assertNotNull(config);
		assertEquals("bar", config.getString("foo"));
	}

	@Test
	public void moduleAccessesApplicationConfiguration() throws Exception {
		assertEquals("test1", injector.getInstance(Key.get(String.class, Names.named("__module_config-applicationstring"))));
	}

	public static interface SomeInterface {
		public void callSomeMethod();
	}

	public static class SomeInterfaceImpl implements SomeInterface {

		@Override
		public void callSomeMethod() {
		}

	}

	@ConfiguredModule("TestModule")
	public static final class TestModule extends AbstractModule {

		@Inject
		@Named("application/application_config_string")
		private String applicationConfigString;

		@Inject
		@Named("#")
		private Config config;

		@Inject
		@Named("#foo")
		private String foo;

		@Override
		protected void configure() {
			bind(SomeInterface.class).to(SomeInterfaceImpl.class);
			bind(String.class).annotatedWith(Names.named("__module_config-foo")).toInstance(foo);
			bind(String.class).annotatedWith(Names.named("__module_config-applicationstring")).toInstance(applicationConfigString);
			bind(Config.class).annotatedWith(Names.named("__module_config-object")).toInstance(config);
		}

		@Override
		public String toString() {
			return "TestModule [applicationConfigString=" + applicationConfigString + ", config=" + config + ", foo=" + foo + "]";
		}

	}
}
