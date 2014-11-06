package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public final class ModuleLoaderModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ModuleLoaderModule.class);

	@Inject
	private ConfigResources resources;

	@Inject
	private AnnotationScanner annotationScanner;

	@Inject
	private Injector injector;

	@Inject
	private Set<ValueConverter> valueConverters;

	ModuleLoaderModule() {

	}

	@Override
	protected void configure() {
		try {
			for (Config moduleConfig : loadModuleConfigs()) {
				bindModule(moduleConfig);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("unchecked")
	private void bindModule(Config moduleConfig) {
		String type = getType(moduleConfig);
		Class<?> moduleClass = getModuleClass(type);
		if (moduleClass != null) {
			if (!Module.class.isAssignableFrom(moduleClass)) {
				throw new IllegalStateException("Module class " + moduleClass + "  for module type " + type + " is not an instance of com.google.inject.Module");
			}
			logger.info("Installing module. Type=" + type + ", class=" + moduleClass);
			Module configuredModule = configureModule(moduleConfig, (Class<? extends Module>) moduleClass);
			install(configuredModule);
		}
	}

	private Module configureModule(final Config moduleConfig, Class<? extends Module> moduleClass) {
		Injector moduleInjector = injector.createChildInjector(new ModuleForConfiguringModules(moduleConfig));
		Module instance = moduleInjector.getInstance(moduleClass);
		return instance;
	}

	private String getType(Config moduleConfig) {
		if (!moduleConfig.hasPath(Filetypes.MODULE_TYPE)) {
			throw new IllegalStateException("Module does not have type:" + moduleConfig);
		}
		return moduleConfig.getString(Filetypes.MODULE_TYPE);
	}

	private Class<?> getModuleClass(String moduleType) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> moduleClass : annotationScanner.getClassesAnnotatedWith(ConfiguredModule.class)) {
			ConfiguredModule annotation = moduleClass.getAnnotation(ConfiguredModule.class);
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
		List<Config> list = new ArrayList<Config>();
		for (Config c : resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
			if (c.hasPath(Filetypes.MODULES)) {
				list.addAll(c.getConfigList(Filetypes.MODULES));
			}
		}
		return list;
	}

	private final class ModuleForConfiguringModules extends AbstractModule {

		private final Config config;

		private BindUtil bindUtil;

		public ModuleForConfiguringModules(Config config) {
			this.config = config;
		}

		@Override
		protected void configure() {
			this.bindUtil = new BindUtil(binder());
			UniqueObjectPathSet objectPaths = new UniqueObjectPathSet();
			for (Entry<String, ConfigValue> entry : config.entrySet()) {
				String configValuePath = entry.getKey();
				objectPaths.add(configValuePath);
				bindConfigValue(configValuePath, entry.getValue());
			}
			bindConfigObjectPaths(objectPaths);
		}

		private void bindConfigValue(String key, ConfigValue value) {
			for (ValueConverter converter : valueConverters) {
				Object result = converter.convert(value);
				if (result != null) {
					bindUtil.bindInstance(converter.getBindingType(), "#" + key, result);
				}
			}
		}

		private void bindConfigObjectPaths(UniqueObjectPathSet objectPaths) {
			bindConfigValue("", config.root());
			for (String objectPath : objectPaths) {
				Config object = config.getConfig(objectPath);
				bindConfigValue(objectPath, object.root());
			}
		}

	}
}
