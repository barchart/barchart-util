package com.barchart.util.test.guice;

import java.util.HashSet;
import java.util.Set;

import com.barchart.util.guice.ComponentActivator;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.typesafe.config.Config;

public class TestInjectorBuilder {

	private final Set<Module> modules;

	private TestInjectorBuilder() {
		modules = new HashSet<Module>();
		modules.add(new ComponentActivator());
	}

	public static TestInjectorBuilder create() {
		return new TestInjectorBuilder();
	}

	public TestInjectorBuilder config(final Config config) {
		modules.add(new TestConfigModule(config));
		return this;
	}

	public TestInjectorBuilder config(final String config) {
		modules.add(new TestConfigModule(config));
		return this;
	}

	public TestInjectorBuilder module(final Module module) {
		modules.add(module);
		return this;
	}

	public TestInjectorBuilder component(final Class<?> type, final String config) {
		modules.add(new TestComponentModule(type, config));
		return this;
	}

	public Injector build() {
		return Guice.createInjector(modules);
	}

}
