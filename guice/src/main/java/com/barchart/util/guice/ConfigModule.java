//package com.barchart.util.guice;
//
//import java.io.File;
//import java.io.FilenameFilter;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.barchart.util.guice.converters.BooleanConverter;
//import com.barchart.util.guice.converters.BooleanListConverter;
//import com.barchart.util.guice.converters.ByteConverter;
//import com.barchart.util.guice.converters.ByteListConverter;
//import com.barchart.util.guice.converters.DoubleConverter;
//import com.barchart.util.guice.converters.DoubleListConverter;
//import com.barchart.util.guice.converters.FloatConverter;
//import com.barchart.util.guice.converters.FloatListConverter;
//import com.barchart.util.guice.converters.IntegerConverter;
//import com.barchart.util.guice.converters.IntegerListConverter;
//import com.barchart.util.guice.converters.LongConverter;
//import com.barchart.util.guice.converters.LongListConverter;
//import com.barchart.util.guice.converters.ShortConverter;
//import com.barchart.util.guice.converters.ShortListConverter;
//import com.barchart.util.guice.converters.StringConverter;
//import com.barchart.util.guice.converters.StringListConverter;
//import com.google.inject.AbstractModule;
//import com.google.inject.TypeLiteral;
//import com.google.inject.name.Names;
//import com.typesafe.config.Config;
//import com.typesafe.config.ConfigFactory;
//import com.typesafe.config.ConfigValue;
//
//public final class ConfigModule extends AbstractModule {
//
//	private static final Logger logger = LoggerFactory.getLogger(ConfigModule.class);
//
//	private static final String DEFAULT_CONFIG_FILENAME = "application.conf";
//
//	private static final String APPLICATION_CONF = "application.conf";
//
//	private static final String COMPONENT_LIST = "component";
//
//	private static final String COMPONENT_TYPE = "type";
//
//	private static final FilenameFilter FILENAME_FILTER = new FilenameFilter() {
//		@Override
//		public boolean accept(File dir, String name) {
//			return name.endsWith(".conf") || name.endsWith(".component");
//		}
//	};
//
//	private final File configDirectory;
//
//	private final String applicationConf;
//
//	private List<ValueConverter> converters;
//
//	public ConfigModule(File configDirectory) {
//		this(configDirectory, APPLICATION_CONF);
//
//	}
//
//	public ConfigModule(File configDirectory, String applicationConf) {
//		if (!configDirectory.isDirectory()) {
//			throw new IllegalArgumentException(configDirectory + " is not a directory");
//		}
//		this.configDirectory = configDirectory;
//		this.applicationConf = applicationConf;
//		converters = new ArrayList<ValueConverter>();
//		converters.add(new StringConverter());
//		converters.add(new BooleanConverter());
//		converters.add(new LongConverter());
//		converters.add(new IntegerConverter());
//		converters.add(new ShortConverter());
//		converters.add(new ByteConverter());
//		converters.add(new DoubleConverter());
//		converters.add(new FloatConverter());
//		// converters.add(new ConfigConverter());
//		converters.add(new ConfigListConverter());
//		converters.add(new StringListConverter());
//		converters.add(new BooleanListConverter());
//		converters.add(new LongListConverter());
//		converters.add(new IntegerListConverter());
//		converters.add(new ShortListConverter());
//		converters.add(new ByteListConverter());
//		converters.add(new DoubleListConverter());
//		converters.add(new FloatListConverter());
//
//	}
//
//	public void addConverter(ValueConverter valueConverter) {
//		converters.add(valueConverter);
//	}
//
//	@Override
//	protected void configure() {
//		bind(ConfigDirectory.class).toInstance(new ConfigDirectory(configDirectory));
//		for (File file : configDirectory.listFiles(FILENAME_FILTER)) {
//			ConfigFile configFile = new ConfigFile(file);
//			configFile.applyBindings();
//		}
//	}
//
//	private void bindConfigs(Config config) {
//		Set<String> configPaths = getConfigPaths(config);
//		for (String key : configPaths) {
//			Config subConfig = config.getConfig(key);
//			bind(Config.class).annotatedWith(Names.named(key)).toInstance(subConfig);
//		}
//	}
//
//	private Set<String> getConfigPaths(Config config) {
//		Set<String> configPaths = new HashSet<String>();
//		for (Entry<String, ConfigValue> entry : config.entrySet()) {
//			addParentObjectPath(configPaths, entry.getKey());
//		}
//		return configPaths;
//	}
//
//	private void addParentObjectPath(Set<String> configPaths, String key) {
//		String parentObjectPath = getParentObjectPath(key);
//		if (!parentObjectPath.isEmpty()) {
//			configPaths.add(parentObjectPath);
//			addParentObjectPath(configPaths, parentObjectPath);
//		}
//	}
//
//	private String getParentObjectPath(String key) {
//		int lastDotIndex = key.lastIndexOf('.');
//		if (lastDotIndex == -1) {
//			return "";
//		} else {
//			return key.substring(0, lastDotIndex);
//		}
//	}
//
//	private class ConfigFile {
//
//		private final String filename;
//
//		private final Config config;
//
//		private String shortname;
//
//		public ConfigFile(File file) {
//			this.filename = file.getName();
//			this.shortname = filename.replaceAll("\\..*$", "");
//			this.config = ConfigFactory.parseFile(file);
//
//		}
//
//		public void applyBindings() {
//
//			applyBindings(shortname + "/");
//
//			if (DEFAULT_CONFIG_FILENAME.equals(filename)) {
//				bind(Config.class).toInstance(config);
//				bind(Config.class).annotatedWith(Names.named("/")).toInstance(config);
//				applyBindings("");
//			}
//
//			bindConfigs(config);
//		}
//
//		private void applyBindings(String prefix) {
//			bind(Config.class).annotatedWith(Names.named(prefix)).toInstance(config);
//			for (Entry<String, ConfigValue> entry : config.entrySet()) {
//				String key = prefix + entry.getKey();
//				ConfigValue value = entry.getValue();
//				bindNamedValue(key, value);
//			}
//		}
//
//		private void bindNamedValue(String key, ConfigValue value) {
//			for (ValueConverter converter : converters) {
//				converter.applyBindings(binder().withSource(filename), key, value);
//			}
//		}
//
//	}
//
//}
