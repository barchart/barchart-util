package com.barchart.util.guice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.encryption.Decrypter;
import com.barchart.util.guice.encryption.EchoDecrypter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public final class GuiceConfigBuilder {

	private static final Logger logger = LoggerFactory.getLogger(GuiceConfigBuilder.class);

	private final ArrayList<Module> modules;

	private final ArrayList<ValueConverter> valueConverters;

	private ConfigResources configResources;

	private Decrypter decrypter;

	private final boolean componentSupport;

	GuiceConfigBuilder(final boolean component) {

		this.componentSupport = component;

		this.modules = new ArrayList<Module>();
		this.valueConverters = new ArrayList<ValueConverter>();

	}

	/**
	 * Create a new injector supporting automatic configuration injection, dynamic module configuration and component
	 * discovery.
	 */
	public static GuiceConfigBuilder create() {
		return new GuiceConfigBuilder(true);
	}

	/**
	 * Create a new injector without dynamic module configuration or component discovery. This avoids classpath scanning
	 * overhead on build.
	 */
	public static GuiceConfigBuilder createBasic() {
		return new GuiceConfigBuilder(false);
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
		return setDirectory(directoryName, false);
	}

	public GuiceConfigBuilder setDirectory(final String directoryName, final boolean classpathFallback) {

		try {

			if (classpathFallback) {
				this.configResources = new MergedResources(
						new DirectoryResources(new File(directoryName)),
						new ClassPathResources());
			} else {
				this.configResources = new DirectoryResources(new File(directoryName));
			}

			return this;

		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

	}

	public GuiceConfigBuilder setConfigResources(final ConfigResources configResources) {
		this.configResources = configResources;
		return this;
	}

	public GuiceConfigBuilder setDecrypter(final Decrypter decrypter) {
		this.decrypter = decrypter;
		return this;
	}

	public Injector build() throws Exception {
		return build(Guice.createInjector());
	}

	public Injector build(final Injector parentInjector) {
		Injector injector = parentInjector.createChildInjector(new BasicModule(), new ComponentActivator());
		injector = injector.createChildInjector(injector.getInstance(ValueConverterModule.class));
		injector = injector.createChildInjector(injector.getInstance(ConfigValueBinderModule.class));
		injector = injector.createChildInjector(modules);
		if (componentSupport) {
			injector = injector.createChildInjector(injector.getInstance(ModuleLoaderModule.class));
			injector = injector.createChildInjector(injector.getInstance(ComponentModule.class));
		}
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

				if (decrypter == null) {
					bind(Decrypter.class).to(EchoDecrypter.class).in(Singleton.class);
				} else {
					bind(Decrypter.class).toInstance(decrypter);
				}

			} catch (final Exception e) {
				throw new RuntimeException(e);
			}

		}

	}

}
