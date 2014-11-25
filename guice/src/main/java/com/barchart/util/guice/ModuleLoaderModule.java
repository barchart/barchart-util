package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.typesafe.config.Config;

public final class ModuleLoaderModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ModuleLoaderModule.class);

	@Inject
	private ConfigResources resources;

	@Inject
	private AnnotationScanner annotationScanner;

	@Inject
	private Injector injector;

	@Inject
	private ValueConverterTool valueConverterTool;

	ModuleLoaderModule() {

	}

	@Override
	protected void configure() {
		// FIXME: ? hack to get correct injector in ComponentModule
		bind(ComponentModule.class);
		try {
			for (final Config moduleConfig : loadModuleConfigs()) {
				bindModule(moduleConfig);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("unchecked")
	private void bindModule(final Config moduleConfig) {
		final String type = getType(moduleConfig);
		final Class<?> moduleClass = getModuleClass(type);
		if (moduleClass != null) {
			if (!Module.class.isAssignableFrom(moduleClass)) {
				throw new IllegalStateException("Module class " + moduleClass + "  for module type " + type + " is not an instance of com.google.inject.Module");
			}
			logger.info("Installing module. Type=" + type + ", class=" + moduleClass);
			final Module configuredModule = configureModule(moduleConfig, (Class<? extends Module>) moduleClass);
			install(configuredModule);
		}
	}

	private Module configureModule(final Config moduleConfig, final Class<? extends Module> moduleClass) {
		final Injector moduleInjector = injector.createChildInjector(new ModuleForConfiguringModules(moduleConfig));
		final Module instance = moduleInjector.getInstance(moduleClass);
		return instance;
	}

	private String getType(final Config moduleConfig) {
		if (!moduleConfig.hasPath(Filetypes.MODULE_TYPE)) {
			throw new IllegalStateException("Module does not have type:" + moduleConfig);
		}
		return moduleConfig.getString(Filetypes.MODULE_TYPE);
	}

	private Class<?> getModuleClass(final String moduleType) {
		final List<Class<?>> list = new ArrayList<Class<?>>();
		for (final Class<?> moduleClass : annotationScanner.getConfiguredModuleClasses()) {
			final ConfiguredModule annotation = moduleClass.getAnnotation(ConfiguredModule.class);
			if (moduleType.equals(annotation.value())) {
				list.add(moduleClass);
			}
		}
		if (list.size() == 0) {
			// TODO: Throw exception?
			logger.warn("No @ConfiguredModule classes found for configured module type \"" + moduleType + "\"");
			return null;
		} else if (list.size() > 1) {
			// TODO: Throw exception?
			logger.warn("Multiple @ConfiguredModule classes found for module type: \"" + moduleType + "\".  classes=" + list + ".  Using the first one.");
		}
		return list.get(0);
	}

	private List<Config> loadModuleConfigs() throws Exception {
		final List<Config> list = new ArrayList<Config>();
		for (final Config c : resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
			if (c.hasPath(Filetypes.MODULES)) {
				list.addAll(c.getConfigList(Filetypes.MODULES));
			}
		}
		return list;
	}

	private final class ModuleForConfiguringModules extends AbstractModule {

		private final Config config;

		public ModuleForConfiguringModules(final Config config) {
			this.config = config;
		}

		@Override
		protected void configure() {
			new ConfigBinder(binder(), valueConverterTool).applyBindings(config, "#");
		}

	}
}
