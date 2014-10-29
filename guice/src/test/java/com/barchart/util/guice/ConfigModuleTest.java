package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.inject.Named;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.typesafe.config.Config;

public class ConfigModuleTest {

	private static final File CONFIG_DIRECTORY = new File("./src/test/resources/configmoduletest/conf");

	private static final String CONFIG_ID = "config_id";

	private static final String APPLICATION_CONF = "application.conf";

	private static final String APPLICATION_CONF_NO_COMPONENTS = "application.conf.nocomponents";

	@Test(expected = NullPointerException.class)
	public void constructorFailsIfNull() {
		new ConfigModule(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorFailsIfNotADirectory() {
		new ConfigModule(new File(CONFIG_DIRECTORY, "application.conf"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorFailsIfDoesNotExist() {
		new ConfigModule(new File(CONFIG_DIRECTORY, "doesnotexist"));
	}

	@Test
	public void okayIfComponentListIsEmpty() {
		ConfigModule configModule = new ConfigModule(CONFIG_DIRECTORY, APPLICATION_CONF_NO_COMPONENTS);
		Injector injector = Guice.createInjector(configModule);
		DefaultConfigConsumer consumer = injector.getInstance(DefaultConfigConsumer.class);
		assertEquals("ApplicationConfNoComponents", consumer.config.getString(CONFIG_ID));
	}

	@Test
	public void defaultConfigBinding() {
		ConfigModule configModule = new ConfigModule(CONFIG_DIRECTORY);
		Injector injector = Guice.createInjector(configModule);
		DefaultConfigConsumer consumer = injector.getInstance(DefaultConfigConsumer.class);
		assertEquals("ApplicationConf", consumer.config.getString(CONFIG_ID));
	}
	
	@Test
	public void componentListOfDistinctComponentTypes() {
		ConfigModule configModule = new ConfigModule(CONFIG_DIRECTORY);
		Injector injector = Guice.createInjector(configModule);
		assertEquals("ComponentConfig1", injector.getInstance(Component1.class).config.getString(CONFIG_ID));
		assertEquals("ComponentConfig2", injector.getInstance(Component2.class).config.getString(CONFIG_ID));
		assertEquals("ComponentConfig3", injector.getInstance(Component3.class).config.getString(CONFIG_ID));
	}

	private static class DefaultConfigConsumer {
		@Inject
		Config config;
	}
	
	private static class Component1 {
		@Inject
		@Named("Component1")
		Config config;		
	}
	
	private static class Component2 {
		@Inject
		@Named("Component2")
		Config config;		
	}
	
	private static class Component3 {
		@Inject
		@Named("Component3")
		Config config;
	}
	
	
}
