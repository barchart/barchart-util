package com.barchart.util.guice;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public final class GuiceConfigBuilder {

	private static final Logger logger = LoggerFactory.getLogger(GuiceConfigBuilder.class);

	private final ArrayList<Module> modules;

	private final ArrayList<ValueConverter> valueConverters;

	private ConfigResources configResources;

	GuiceConfigBuilder() {

		this.modules = new ArrayList<Module>();
		this.valueConverters = new ArrayList<ValueConverter>();

		addModule(new BasicModule());
		addModule(new ComponentActivator());

	}

	public static GuiceConfigBuilder create() {
		return new GuiceConfigBuilder();
	}

	public GuiceConfigBuilder addModule(final Module module) {
		modules.add(module);
		return this;
	}

	public GuiceConfigBuilder addValueConverter(final ValueConverter valueConverter) {
		// TODO: This doesn't do anything anymore. Either remove it, do the
		// multibindings in the BasicModule, or somehow pass it into the
		// ValueConverterModule
		valueConverters.add(valueConverter);
		return this;
	}

	public GuiceConfigBuilder setDirectory(final String directoryName) {
		this.configResources = new DirectoryResources(new File(directoryName));
		return this;
	}

	public GuiceConfigBuilder setConfigResources(final ConfigResources configResources) {
		this.configResources = configResources;
		return this;
	}

	public Injector build() throws Exception {
		Injector injector = Guice.createInjector(modules);
		injector = injector.createChildInjector(injector.getInstance(ValueConverterModule.class));
		injector = injector.createChildInjector(injector.getInstance(ConfigValueBinderModule.class));
		injector = injector.createChildInjector(injector.getInstance(ModuleLoaderModule.class));
		injector = injector.createChildInjector(injector.getInstance(ComponentModule.class));
		return injector;
	}

	private class BasicModule extends AbstractModule {

		@Override
		protected void configure() {
			try {
				if (configResources == null) {
					bind(ConfigResources.class).toInstance(new ClassPathResources());
				} else {
					bind(ConfigResources.class).toInstance(configResources);
				}
				final ComponentScope componentScope = new ComponentScope();
				bindScope(ComponentScoped.class, componentScope);
				bind(ComponentScope.class).toInstance(componentScope);

			} catch (final Exception e) {
				throw new RuntimeException(e);
			}

		}

	}
}
