package com.barchart.util.guice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.barchart.util.guice.converters.BasicValueConverters;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.typesafe.config.Config;

public final class GuiceConfigBuilder {

	private final ArrayList<Module> modules;

	private final ArrayList<ValueConverter> valueConverters;

	private File directory;

	private ConfigResources configResources;

	GuiceConfigBuilder() {
		this.modules = new ArrayList<Module>();
		this.valueConverters = new ArrayList<ValueConverter>();
	}

	public static GuiceConfigBuilder create() {
		return new GuiceConfigBuilder();
	}

	public GuiceConfigBuilder addModule(Module module) {
		modules.add(module);
		return this;
	}

	public GuiceConfigBuilder addValueConverter(ValueConverter valueConverter) {
		valueConverters.add(valueConverter);
		return this;
	}

	public GuiceConfigBuilder setDirectory(String directoryName) {
		this.configResources = new DirectoryResources(new File(directoryName));
		return this;
	}

	public GuiceConfigBuilder setConfigResources(ConfigResources configResources) {
		this.configResources = configResources;
		return this;
	}

	public Injector build() throws Exception {
		addDefaultValueConverters();
		List<Config> configFiles = readConfigFiles();
		modules.add(new BasicModule());
		modules.add(new ConfigValueBinderModule(configFiles, valueConverters));
		modules.add(new ComponentModule(configFiles, valueConverters, new AnnotationScanner()));
		return Guice.createInjector(modules);
	}

	private void addDefaultValueConverters() {
//		valueConverters.add(new StringConverter());
//		valueConverters.add(new BooleanConverter());
//		valueConverters.add(new LongConverter());
//		valueConverters.add(new IntegerConverter());
//		valueConverters.add(new ShortConverter());
//		valueConverters.add(new ByteConverter());
//		valueConverters.add(new DoubleConverter());
//		valueConverters.add(new FloatConverter());
//		valueConverters.add(new ConfigConverter());
//		valueConverters.add(new ConfigListConverter());
//		valueConverters.add(new StringListConverter());
//		valueConverters.add(new BooleanListConverter());
//		valueConverters.add(new LongListConverter());
//		valueConverters.add(new IntegerListConverter());
//		valueConverters.add(new ShortListConverter());
//		valueConverters.add(new ByteListConverter());
//		valueConverters.add(new DoubleListConverter());
//		valueConverters.add(new FloatListConverter());
		valueConverters.addAll(new BasicValueConverters().getList());
	}

	private List<Config> readConfigFiles() throws Exception {
		List<Config> list = new ArrayList<Config>();
		for (String resourceName : configResources.listResources()) {
			if (resourceName.endsWith(Filetypes.CONFIG_FILE_EXTENSION)) {
				list.add(configResources.readConfig(resourceName));
			}
			if (resourceName.endsWith(Filetypes.COMPONENT_FILE_EXTENSION)) {
				list.add(configResources.readConfig(resourceName));
			}
		}
		return list;
	}

	private class BasicModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(ConfigResources.class).toInstance(configResources);
		}

	}
}
