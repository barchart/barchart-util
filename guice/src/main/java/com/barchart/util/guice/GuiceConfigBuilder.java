package com.barchart.util.guice;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.barchart.util.guice.converters.BooleanConverter;
import com.barchart.util.guice.converters.BooleanListConverter;
import com.barchart.util.guice.converters.ByteConverter;
import com.barchart.util.guice.converters.ByteListConverter;
import com.barchart.util.guice.converters.ConfigConverter;
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
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public final class GuiceConfigBuilder {

	private final ArrayList<Module> modules;

	private final ArrayList<ValueConverter> valueConverters;

	private File directory;

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
		this.directory = new File(directoryName);
		return this;
	}

	public Injector build() {
		addDefaultValueConverters();
		List<Config> configFiles = readConfigFiles();
		modules.add(new BasicModule());
		modules.add(new ConfigValueBinderModule(configFiles, valueConverters));
		modules.add(new ComponentModule(configFiles, valueConverters, new AnnotationScanner()));
		return Guice.createInjector(modules);
	}

	private void addDefaultValueConverters() {
		valueConverters.add(new StringConverter());
		valueConverters.add(new BooleanConverter());
		valueConverters.add(new LongConverter());
		valueConverters.add(new IntegerConverter());
		valueConverters.add(new ShortConverter());
		valueConverters.add(new ByteConverter());
		valueConverters.add(new DoubleConverter());
		valueConverters.add(new FloatConverter());
		valueConverters.add(new ConfigConverter());
		valueConverters.add(new ConfigListConverter());
		valueConverters.add(new StringListConverter());
		valueConverters.add(new BooleanListConverter());
		valueConverters.add(new LongListConverter());
		valueConverters.add(new IntegerListConverter());
		valueConverters.add(new ShortListConverter());
		valueConverters.add(new ByteListConverter());
		valueConverters.add(new DoubleListConverter());
		valueConverters.add(new FloatListConverter());
	}

	private List<Config> readConfigFiles() {
		ConfigDirectory configDirectory = new ConfigDirectory(directory);
		List<Config> list = new ArrayList<Config>();
		for (File file : configDirectory.listFiles(".conf")) {
			list.add(ConfigFactory.parseFile(file));
		}
		for (File file : configDirectory.listFiles(".component")) {
			list.add(ConfigFactory.parseFile(file));
		}
		for (File file : configDirectory.listFiles(".module")) {
			list.add(ConfigFactory.parseFile(file));
		}
		return list;
	}

	private class BasicModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(ConfigDirectory.class).toInstance(new ConfigDirectory(directory));
		}

	}
}
