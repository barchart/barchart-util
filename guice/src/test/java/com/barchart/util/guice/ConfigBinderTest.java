package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigBinderTest {

	@Test
	public void testWithPoundPrefix() {
		final Config config = ConfigFactory.parseString("{key1 = hello, key2 = world, key3 = 42 }");
		System.out.println("config: " + config);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				ConfigBinder configBinder = new ConfigBinder(binder(), ValueConverterTool.defaultValueConverterTool());
				configBinder.applyBindings(config, "#");
			}
		});
		assertEquals(config, injector.getInstance(Key.get(Config.class, Names.named("#"))));
		assertEquals("hello", injector.getInstance(Key.get(String.class, Names.named("#key1"))));
		assertEquals("world", injector.getInstance(Key.get(String.class, Names.named("#key2"))));
		assertEquals(Integer.valueOf(42), injector.getInstance(Key.get(Integer.class, Names.named("#key3"))));
	}
	
	@Test
	public void testWithNoPrefix() {
		final Config config = ConfigFactory.parseString("{key1 = hello, key2 = world, key3 = 42 }");
		System.out.println("config: " + config);
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				ConfigBinder configBinder = new ConfigBinder(binder(), ValueConverterTool.defaultValueConverterTool());
				configBinder.applyBindings(config);
			}
		});
		
		assertEquals(config, injector.getInstance(Key.get(Config.class, Names.named(""))));
		assertEquals("hello", injector.getInstance(Key.get(String.class, Names.named("key1"))));
		assertEquals("world", injector.getInstance(Key.get(String.class, Names.named("key2"))));
		assertEquals(Integer.valueOf(42), injector.getInstance(Key.get(Integer.class, Names.named("key3"))));
	}
	
}
