package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

final class ComponentModule extends AbstractModule {

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
		HashMultiset<Class<?>> bindingTypeCounter = HashMultiset.create();
		for (Config componentConfig : loadComponentConfigs()) {
			String type = getType(componentConfig);
			Class<?> componentClass = getComponentClass(type);
			if (componentClass != null) {
				ComponentConfigurationModule configurationModule = new ComponentConfigurationModule(componentClass, componentConfig, bindingTypeCounter);
				install(configurationModule);
			}
		}
		bindMultibindings(bindingTypeCounter);

	}

	private void bindMultibindings(HashMultiset<Class<?>> bindingTypeCounter) {
		for (Multiset.Entry<Class<?>> entry : bindingTypeCounter.entrySet()) {
			Class<?> bindingType = entry.getElement();
			@SuppressWarnings("unchecked")
			Multibinder<Object> setBinder = (Multibinder<Object>) Multibinder.newSetBinder(binder(), bindingType);
			for (int i = 0; i < entry.getCount(); i++) {
				setBinder.addBinding().to(Key.get(bindingType, indexed(i)));
			}
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




	private final class ComponentConfigurationModule extends PrivateModule {

		private final Config config;

		private final Class<?> componentClass;


		private final HashMultiset<Class<?>> bindingTypeCounter;

		private BindUtil bindUtil;

		public ComponentConfigurationModule(Class<?> componentClass, Config config, HashMultiset<Class<?>> bindingTypeCounter) {
			this.componentClass = componentClass;
			this.config = config;
			this.bindingTypeCounter = bindingTypeCounter;
		}

		@Override
		protected void configure() {
			this.bindUtil = new BindUtil(binder());
			bind(componentClass).in(Singleton.class);
			bindConfiguration();
			final String name = getName(config);
			for (Class<?> bindingType : CastableTypes.of(componentClass)) {
				if (name != null) {
					bindByName(name, bindingType);
				}
				exposeToComponentList(bindingType);
			}
		}

		private void exposeToComponentList(Class<?> bindingType) {
			int index = bindingTypeCounter.add(bindingType, 1);
			@SuppressWarnings("unchecked")
			LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) bind(Key.get(bindingType, indexed(index)));
			bindingBuilder.to(componentClass);
			expose(Key.get(bindingType, indexed(index)));
		}

		private void bindByName(String name, Class<?> bindingType) {
			@SuppressWarnings("unchecked")
			LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) binder() //
					.withSource(config) //
					.bind(bindingType) //
					.annotatedWith(Names.named(name));
			bindingBuilder.to(componentClass);
			expose(bindingType).annotatedWith(Names.named(name));
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

	private Indexed indexed(int index) {
		return new IndexedImpl(index);
	}
}
