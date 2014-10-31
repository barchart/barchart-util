package com.barchart.util.guice.component;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.ConfigDirectory;
import com.barchart.util.guice.ValueConverter;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ConfigurationModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationModule.class);

	private static final String COMPONENT_LIST = "component";

	private static final String MODULE_LIST = "module";

	private static final FilenameFilter CONF_FILE_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".conf");
		}
	};

	private static final FilenameFilter COMPONET_FILE_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".component");
		}
	};

	private static final FilenameFilter MODULE_FILE_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".module");
		}
	};

	@Inject
	private Injector injector;

	@Inject
	private ConfigDirectory configDirectory;

	@Inject
	private AnnotationScanner annotationScanner;

	@Inject
	private List<ValueConverter> valueConverters;

	@Override
	protected void configure() {

		List<ComponentConfig> componentConfigs = new ArrayList<ComponentConfig>();
		List<ModuleConfig> moduleConfigs = new ArrayList<ModuleConfig>();

		for (File file : configDirectory.listFiles(CONF_FILE_FILTER)) {
			Config config = parseFile(file);
			install(new ConfigFieldBindingModule(config, getSimpleFilename(file), valueConverters));
			componentConfigs.addAll(getComponentList(config));
			moduleConfigs.addAll(getModuleList(config));
		}

		for (File file : configDirectory.listFiles(COMPONET_FILE_FILTER)) {
			Config config = parseFile(file);
			componentConfigs.add(new ComponentConfig(config));
		}

		for (File file : configDirectory.listFiles(MODULE_FILE_FILTER)) {
			Config config = parseFile(file);
			moduleConfigs.add(new ModuleConfig(config));
		}

		installModules(moduleConfigs);
		bindComponents(componentConfigs);

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

	private Collection<ModuleConfig> getModuleList(Config config) {
		ArrayList<ModuleConfig> list = new ArrayList<ModuleConfig>();
		if (config.hasPath(MODULE_LIST)) {
			for (Config c : config.getConfigList(MODULE_LIST)) {
				list.add(new ModuleConfig(c));
			}
		}
		return list;
	}

	private Config parseFile(File file) {
		return ConfigFactory.parseFile(file);
	}

	private void installModules(List<ModuleConfig> moduleConfigs) {
		
	}

	private void bindComponents(List<ComponentConfig> componentConfigs) {
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

		for (ComponentConfig config : componentConfigs) {
			String type = config.getType();
			String name = config.getName();
			Class<?> clazz = componentTypeMap.get(type);
			
			if (clazz == null) {
				throw new IllegalStateException("Cannot configure component of type + \"" + type + "\" because no component of this type exists on the classpath.");
			}

			Injector componentInjector = injector.createChildInjector(new ConfigFieldBindingModule(config.getConfig(), "#", valueConverters ));
			Object instance = componentInjector.getInstance(clazz);

			@SuppressWarnings("unchecked")
			AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) bind(clazz);
			if (name != null) {
				logger.info("Binding: " + clazz + " named " + name + " to " + instance);
				bindingBuilder.annotatedWith(Names.named(name)).toInstance(instance);
			}

		}

	}

}
