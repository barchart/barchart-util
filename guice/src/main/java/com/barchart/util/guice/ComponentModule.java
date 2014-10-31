package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.PrivateModule;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public final class ComponentModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ComponentModule.class);

	private static final String COMPONENT_TYPE = "type";

	private static final String COMPONENT_NAME = "name";

	private final List<Config> configFiles;

	private final ArrayList<ValueConverter> valueConverters;

	private final AnnotationScanner annotationScanner;

	public ComponentModule(List<Config> configFiles, ArrayList<ValueConverter> valueConverters, AnnotationScanner annotationScanner) {
		this.configFiles = configFiles;
		this.valueConverters = valueConverters;
		this.annotationScanner = annotationScanner;
	}

	@Override
	protected void configure() {
		for (Config componentConfig : loadComponentConfigs()) {
			String type = getType(componentConfig);
			String name = getName(componentConfig);
			install(new ComponentConfigurationModule(type, name, componentConfig));
		}
	}

	private List<Config> loadComponentConfigs() {
		List<Config> list = new ArrayList<Config>();
		for (Config configFile : configFiles) {
			if (Filetypes.isConfigFile(configFile)) {
				list.addAll(getComponentList(configFile));
			}
			if (Filetypes.isComponentFile(configFile)) {
				list.add(configFile);
			}
		}
		return list;
	}

	private Collection<Config> getComponentList(Config configFile) {
		List<Config> list = new ArrayList<Config>();
		if (configFile.hasPath(Filetypes.CONFIG_LIST)) {
			list.addAll(configFile.getConfigList(Filetypes.CONFIG_LIST));
		}
		return list;
	}

	private final class ComponentConfigurationModule extends PrivateModule {

		private final Config config;
		private final String type;
		private final String name;

		public ComponentConfigurationModule(String type, String name, Config config) {
			this.type = type;
			this.name = name;
			this.config = config;
		}

		@Override
		protected void configure() {
			bindConfiguration();

			Class<?> componentClass = getComponentClass(type);
			for (Class<?> bindingType : getBindingTypesForClass(componentClass)) {
				
				@SuppressWarnings("unchecked")
				LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) binder() //
					.withSource(config) //
					.bind(bindingType) //
					.annotatedWith(Names.named(name));
				bindingBuilder.to(componentClass).in(Singleton.class);
				
				expose(bindingType).annotatedWith(Names.named(name));
			}

		}

		// Return a list containing all the types we should bind to. Include
		// this class, all supertypes and implemented interfaces. If this class
		// is null, then return an emptylist
		private List<Class<?>> getBindingTypesForClass(Class<?> componentClass) {
			List<Class<?>> list = new ArrayList<Class<?>>();
			for (Class<?> clazz : getInclusiveSuperclasses(componentClass)) {
				list.add(clazz);
				list.addAll(Arrays.asList(clazz.getInterfaces()));
			}
			return list;
		}

		// Return a collection of this class (if not null, and all superclasses)
		private Collection<Class<?>> getInclusiveSuperclasses(Class<?> clazz) {
			List<Class<?>> list = new ArrayList<Class<?>>();
			while (clazz != null) {
				list.add(clazz);
				clazz = clazz.getSuperclass();
			}
			return list;
		}

		private void bindConfiguration() {
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
				converter.applyBindings(binder(), "#" + key, value);
			}
		}

		private void bindConfigObjectPaths(UniqueObjectPathSet objectPaths) {
			bindConfigValue("#", config.root());

			for (String objectPath : objectPaths) {
				Config object = config.getConfig(objectPath);
				bindConfigValue(objectPath, object.root());
			}
		}

		private Class<?> getComponentClass(String componentType) {
			List<Class<?>> list = new ArrayList<Class<?>>();
			for (Class<?> componentClass : annotationScanner.getClassesAnnotatedWith(Component.class)) {
				Component annotation = componentClass.getAnnotation(Component.class);
				if (componentType.equals(annotation.value())) {
					list.add(componentClass);
				}
			}
			if (list.size() == 0) {
				// TODO: Throw exception?
				logger.warn("No @Component classes found for configured component type \"" + componentType + "\"");
				return null;
			} else if (list.size() > 1) {
				// TODO: Throw exception?
				logger.warn("Multiple @Component classes found for component type: \"" + componentType + "\".  classes=" + list + ".  Using the first one.");
			}
			return list.get(0);
		}

	}

	private static String getType(Config componentConfig) {
		if (!componentConfig.hasPath(COMPONENT_TYPE)) {
			throw new IllegalStateException("Component does not have type:" + componentConfig);
		}
		return componentConfig.getString(COMPONENT_TYPE);
	}

	private static String getName(Config componentConfig) {
		if (!componentConfig.hasPath(COMPONENT_NAME)) {
			return null;
		} else {
			return componentConfig.getString(COMPONENT_NAME);
		}
	}

}
