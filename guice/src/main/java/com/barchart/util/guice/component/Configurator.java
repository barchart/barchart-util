package com.barchart.util.guice.component;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.ConfigDirectory;
import com.barchart.util.guice.ConfigListConverter;
import com.barchart.util.guice.ValueConverter;
import com.barchart.util.guice.converters.BooleanConverter;
import com.barchart.util.guice.converters.BooleanListConverter;
import com.barchart.util.guice.converters.ByteConverter;
import com.barchart.util.guice.converters.ByteListConverter;
import com.barchart.util.guice.converters.DoubleConverter;
import com.barchart.util.guice.converters.DoubleListConverter;
import com.barchart.util.guice.converters.FloatConverter;
import com.barchart.util.guice.converters.FloatListConverter;
import com.barchart.util.guice.converters.IntegerConverter;
import com.barchart.util.guice.converters.IntegerListConverter;
import com.barchart.util.guice.converters.LongConverter;
import com.barchart.util.guice.converters.LongListConverter;
import com.barchart.util.guice.converters.ShortConverter;
import com.barchart.util.guice.converters.ShortListConverter;
import com.barchart.util.guice.converters.StringConverter;
import com.barchart.util.guice.converters.StringListConverter;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;
import com.google.inject.util.Providers;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class Configurator {

	private static final Logger logger = LoggerFactory.getLogger(Configurator.class);

	private static final String CONF_FILE_EXTENSION = "conf";

	private static final String COMPONENT_FILE_EXTENSION = "component";

	private static final String MODULE_FILE_EXTENSION = "module";

	private static final String COMPONENT_LIST = "component";

	private static final String DEFAULT_CONFIG_FILENAME = "application.conf";

	private final ConfigDirectory directory;

	private final List<ValueConverter> valueConverters;

	private final AnnotationScanner annotationScanner;

	public Configurator(File directory) {
		this.annotationScanner = new AnnotationScanner();
		this.directory = new ConfigDirectory(directory);
		this.valueConverters = buildDefaultValueConverters();
	}

	public Injector get() {
		ConfigLoader configLoader = new ConfigLoader(directory);

		Injector configurationInjector = createConfigurationInjector(configLoader);

		directory.listFiles(COMPONENT_FILE_EXTENSION);

		return null;
	}

	private Injector createConfigurationInjector(final ConfigLoader configLoader) {
		return Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				for (ConfigField configField : configLoader.getConfigFields()) {
					for (ValueConverter valueConverter : valueConverters) {
						valueConverter.applyBindings(binder().withSource(configField.getFile()), getSimpleFilename(configField.getFile()) + "/" + configField.getKey(), configField.getValue());
					}
				}
			}
		});
	}

	/*
	 * 
	 * Step 1) Load all configuration files. 3 types: Configuration File,
	 * ComponentFile, and Module file Step 2) Convert Files to intermediate
	 * objects: ConfigurationEntry, ComponentEntry, ModuleEntry
	 */
	public Injector configureInjector() {
		final List<ComponentConfig> componentConfigs = new ArrayList<ComponentConfig>();
		// List<ModuleConfig> moduleConfigs = new ArrayList<ModuleConfig>();
		List<Module> modules = new ArrayList<Module>();
		for (File confFile : directory.listFiles(CONF_FILE_EXTENSION)) {
			Config config = ConfigFactory.parseFile(confFile);
			componentConfigs.addAll(getComponentList(config));
			modules.add(new ConfigFieldBindingModule(config, getSimpleFilename(confFile) + "/", valueConverters));
			if (confFile.getName().equals("application.conf")) {
				modules.add(new ConfigFieldBindingModule(config, "", valueConverters));
			}
		}

		for (File componentFile : directory.listFiles(COMPONENT_FILE_EXTENSION)) {
			Config config = ConfigFactory.parseFile(componentFile);
			componentConfigs.add(new ComponentConfig(config));
		}

		final Injector configurationInjector = Guice.createInjector(modules);

		ReplacementModule replacementModule = new ReplacementModule(componentConfigs);
		Injector replacementInjector = configurationInjector.createChildInjector(replacementModule);
		
		final Map<Key<?>, Provider<Object>> componentKeys = createComponents(replacementInjector, componentConfigs);

		Injector componentInjector = configurationInjector.createChildInjector(new AbstractModule() {
			@Override
			protected void configure() {
				for (Map.Entry<Key<?>, Provider<Object>> entry : componentKeys.entrySet()) {
					@SuppressWarnings("unchecked")
					
					AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) bind(entry.getKey());
					logger.info("Binding: " + entry.getKey() + " to " + entry.getValue());
					Provider<Object> provider = entry.getValue();
					bindingBuilder.toProvider(provider);
				}
			}
		});
		replacementModule.setComponentInjector(componentInjector);
		logger.info("Done creating components");
		return componentInjector;
	}

	private Map<Key<?>, Provider<Object>> createComponents(final Injector configurationInjector, List<ComponentConfig> componentConfigs) {
		Map<Key<?>, Provider<Object>> componentKeys = new HashMap<Key<?>, Provider<Object>>();
		Map<String, Class<?>> componentTypeMap = getComponentTypeMap();
		for (ComponentConfig componentConfig : componentConfigs) {
			String type = componentConfig.getType();
			Class<?> clazz = componentTypeMap.get(type);
			if (clazz == null) {
				throw new IllegalStateException("Cannot configure component of type + \"" + type
						+ "\" because no component of this type exists on the classpath.");
			}
			Injector componentInjector = configurationInjector.createChildInjector(new ConfigFieldBindingModule(componentConfig.getConfig(), "#",
					valueConverters));
			String name = componentConfig.getName();
//			Object instance = componentInjector.getInstance(clazz);
//			logger.info("Instantiated component: " + instance + ", hash: " + instance.hashCode());

			if (name != null) {
				final Key<?> key = Key.get(clazz, Names.named(name));
				componentKeys.put(key, new Provider<Object>() {
					@Override
					public Object get() {
						return configurationInjector.getInstance((Key<Object>)key);
					}
					
				});
			}
		}
		return componentKeys;
	}

	/**
	 * 
	 * @return map from component type strings to component class
	 */
	private Map<String, Class<?>> getComponentTypeMap() {
		Map<String, Class<?>> componentTypeMap = new HashMap<String, Class<?>>();
		for (Class<?> clazz : annotationScanner.getClassesAnnotatedWith(Component.class)) {
			Component annotation = clazz.getAnnotation(Component.class);
			String type = annotation.value();
			if (componentTypeMap.containsKey(type)) {
				logger.error("Duplicate Components types on classpath: " + type);
			} else {
				componentTypeMap.put(type, clazz);
			}
		}
		return componentTypeMap;
	}

	public void addTypeConverter(ValueConverter valueConverter) {
		valueConverters.add(valueConverter);
	}

	private List<ValueConverter> getValueConverters() {
		return Collections.unmodifiableList(new ArrayList<ValueConverter>(valueConverters));
	}

	private String getSimpleFilename(File file) {
		return file.getName().replace("\\..*$", "");
	}

	private List<ComponentConfig> getComponentList(Config config) {
		ArrayList<ComponentConfig> list = new ArrayList<ComponentConfig>();
		if (config.hasPath(COMPONENT_LIST)) {
			for (Config c : config.getConfigList(COMPONENT_LIST)) {
				list.add(new ComponentConfig(c));
			}
		}
		return list;
	}

	private static List<ValueConverter> buildDefaultValueConverters() {
		List<ValueConverter> converters = new ArrayList<ValueConverter>();
		converters.add(new StringConverter());
		converters.add(new BooleanConverter());
		converters.add(new LongConverter());
		converters.add(new IntegerConverter());
		converters.add(new ShortConverter());
		converters.add(new ByteConverter());
		converters.add(new DoubleConverter());
		converters.add(new FloatConverter());
		// converters.add(new ConfigConverter());
		// converters.add(new ConfigListConverter());
		converters.add(new StringListConverter());
		converters.add(new BooleanListConverter());
		converters.add(new LongListConverter());
		converters.add(new IntegerListConverter());
		converters.add(new ShortListConverter());
		converters.add(new ByteListConverter());
		converters.add(new DoubleListConverter());
		converters.add(new FloatListConverter());
		return converters;
	}
	
	private  final class ReplacementModule extends AbstractModule {

		private Injector replacementInjector ;
		private List<ComponentConfig> componentConfigs;

		public ReplacementModule(List<ComponentConfig> componentConfigs) {
			this.componentConfigs = componentConfigs;
			this.replacementInjector = null;
		}
		
		public void setComponentInjector(Injector componentInjector) {
			this.replacementInjector  = componentInjector;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected void configure() {
			Map<String, Class<?>> componentTypeMap = getComponentTypeMap();
			for (ComponentConfig componentConfig : componentConfigs) {
				String type = componentConfig.getType();
				Class<?> clazz = componentTypeMap.get(type);
				if (clazz == null) {
					throw new IllegalStateException("Cannot configure component of type + \"" + type
							+ "\" because no component of this type exists on the classpath.");
				}
				String name = componentConfig.getName();
				if (name != null) {
					Key<?> key = Key.get(clazz, Names.named(name));
					LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) bind(key);
					bindingBuilder.toProvider(new ComponentProvider((Key<Object>)key));
				}
			}
		}
		
		private final class ComponentProvider implements Provider<Object> {

			private Key<Object> key;

			public ComponentProvider(Key<Object> key) {
				this.key = key;
			}
			
			@Override
			public Object get() {
				logger.info("Get() " + key);
				if (replacementInjector  == null) {
					throw new IllegalStateException("Set the replacementInjector first");
				}
				return replacementInjector .getInstance(key);
			}
			
		}
		
	}
}
