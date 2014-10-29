package com.barchart.util.guice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

public final class ConfigModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ConfigModule.class);

	private static final String APPLICATION_CONF = "application.conf";

	private static final String COMPONENT_LIST = "component";

	private static final String COMPONENT_TYPE = "type";

	private static final FilenameFilter FILENAME_FILTER = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".conf") || name.endsWith(".component");
		}
	};

	private final File configDirectory;

	private final String applicationConf;

	public ConfigModule(File configDirectory) {
		this(configDirectory, APPLICATION_CONF);

	}

	public ConfigModule(File configDirectory, String applicationConf) {
		if (!configDirectory.isDirectory()) {
			throw new IllegalArgumentException(configDirectory + " is not a directory");
		}
		this.configDirectory = configDirectory;
		this.applicationConf = applicationConf;
		addTypeConverter(new NumberConverter());
	}

	private <T> void addTypeConverter(ValueConveter<T> numberConverter) {

	}

	@Override
	protected void configure() {
		bind(ConfigDirectory.class).toInstance(new ConfigDirectory(configDirectory));
		bind(Config.class).toInstance(getApplicationConf());
		File[] configFiles = configDirectory.listFiles(FILENAME_FILTER);
		for (File file : configFiles) {
			bindValues(file);
		}

	}

	private void bindValues(File file) {
		Config config = ConfigFactory.parseFile(file);

		bindConfigs(config);

		for (Entry<String, ConfigValue> entry : config.entrySet()) {
			bindNamedValue(entry.getKey(), entry.getValue());
		}
	}

	private void bindConfigs(Config config) {
		Set<String> configPaths = getConfigPaths(config);
		for (String key : configPaths) {
			Config subConfig = config.getConfig(key);
			bind(Config.class).annotatedWith(Names.named(key)).toInstance(subConfig);
		}
	}

	private Set<String> getConfigPaths(Config config) {
		Set<String> configPaths = new HashSet<String>();
		for (Entry<String, ConfigValue> entry : config.entrySet()) {
			addParentObjectPath(configPaths, entry.getKey());
		}
		return configPaths;
	}

	private void addParentObjectPath(Set<String> configPaths, String key) {
		String parentObjectPath = getParentObjectPath(key);
		if (!parentObjectPath.isEmpty()) {
			configPaths.add(parentObjectPath);
			addParentObjectPath(configPaths, parentObjectPath);
		}
	}

	private String getParentObjectPath(String key) {
		int lastDotIndex = key.lastIndexOf('.');
		if (lastDotIndex == -1) {
			return "";
		} else {
			return key.substring(0, lastDotIndex);
		}
	}

	private void bindNamedValue(String key, ConfigValue value) {
		switch (value.valueType()) {
		case STRING:
			bind(String.class).annotatedWith(Names.named(key)).toInstance((String) value.unwrapped());
			break;
		case OBJECT:
			break;
		case LIST:
			bindList(key, value);
			break;
		case NULL:
			break;
		case NUMBER:
			bindNumber(key, value);
			break;
		case BOOLEAN:
			bindBoolean(key, value);
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void bindList(String key, ConfigValue value) {
		List<?> unwrapped = (List<?>) value.unwrapped();

		if (areListContentsCastable(unwrapped, String.class)) {
			bind(new TypeLiteral<List<String>>() {
			}).annotatedWith(Names.named(key)).toInstance((List<String>) unwrapped);
		}

		if (areListContentsCastable(unwrapped, Boolean.class)) {
			bind(new TypeLiteral<List<Boolean>>() {
			}).annotatedWith(Names.named(key)).toInstance((List<Boolean>) unwrapped);
		}

		if (areListContentsCastable(unwrapped, HashMap.class)) {
			bind(new TypeLiteral<List<Config>>() {
			}).annotatedWith(Names.named(key)).toInstance((List<Config>) value.atKey(key).getConfigList(key));
		}

		if (areListContentsCastable(unwrapped, Number.class)) {
			bind(new TypeLiteral<List<Long>>() {
			}).annotatedWith(Names.named(key)).toInstance(convertToLongList((List<Number>) unwrapped));

			bind(new TypeLiteral<List<Integer>>() {
			}).annotatedWith(Names.named(key)).toInstance(convertToIntegerList((List<Number>) unwrapped));

			bind(new TypeLiteral<List<Short>>() {
			}).annotatedWith(Names.named(key)).toInstance(convertToShortList((List<Number>) unwrapped));

			bind(new TypeLiteral<List<Byte>>() {
			}).annotatedWith(Names.named(key)).toInstance(convertToByteList((List<Number>) unwrapped));

			bind(new TypeLiteral<List<Double>>() {
			}).annotatedWith(Names.named(key)).toInstance(convertToDoubleList((List<Number>) unwrapped));

			bind(new TypeLiteral<List<Float>>() {
			}).annotatedWith(Names.named(key)).toInstance(convertToFloatList((List<Number>) unwrapped));

		}

	}

	private List<Long> convertToLongList(List<Number> unwrapped) {
		ArrayList<Long> list = new ArrayList<Long>();
		for (Number number : unwrapped) {
			list.add(number.longValue());
		}
		return Collections.unmodifiableList(list);
	}

	private List<Integer> convertToIntegerList(List<Number> unwrapped) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (Number number : unwrapped) {
			list.add(number.intValue());
		}
		return Collections.unmodifiableList(list);
	}

	private List<Short> convertToShortList(List<Number> unwrapped) {
		ArrayList<Short> list = new ArrayList<Short>();
		for (Number number : unwrapped) {
			list.add(number.shortValue());
		}
		return Collections.unmodifiableList(list);
	}

	private List<Byte> convertToByteList(List<Number> unwrapped) {
		ArrayList<Byte> list = new ArrayList<Byte>();
		for (Number number : unwrapped) {
			list.add(number.byteValue());
		}
		return Collections.unmodifiableList(list);
	}

	private List<Double> convertToDoubleList(List<Number> unwrapped) {
		ArrayList<Double> list = new ArrayList<Double>();
		for (Number number : unwrapped) {
			list.add(number.doubleValue());
		}
		return Collections.unmodifiableList(list);
	}

	private List<Float> convertToFloatList(List<Number> unwrapped) {
		ArrayList<Float> list = new ArrayList<Float>();
		for (Number number : unwrapped) {
			list.add(number.floatValue());
		}
		return Collections.unmodifiableList(list);
	}

	private boolean areListContentsCastable(List<?> unwrapped, Class<?> genericParameter) {
		if (unwrapped.isEmpty()) {
			return true;
		} else {
			return genericParameter.isAssignableFrom(unwrapped.get(0).getClass());
		}
	}

	private void bindBoolean(String key, ConfigValue value) {
		bind(Boolean.class).annotatedWith(Names.named(key)).toInstance((Boolean) value.unwrapped());
	}

	private void bindNumber(String key, ConfigValue value) {
		Number number = (Number) value.unwrapped();
		bind(Long.class).annotatedWith(Names.named(key)).toInstance(number.longValue());
		bind(Integer.class).annotatedWith(Names.named(key)).toInstance(number.intValue());
		bind(Short.class).annotatedWith(Names.named(key)).toInstance(number.shortValue());
		bind(Byte.class).annotatedWith(Names.named(key)).toInstance(number.byteValue());
		bind(Double.class).annotatedWith(Names.named(key)).toInstance(number.doubleValue());
		bind(Float.class).annotatedWith(Names.named(key)).toInstance(number.floatValue());
	}

	private Map<String, Config> findComponentConfigList() {
		Map<String, Config> map = new HashMap<String, Config>();
		Config applicationConf = getApplicationConf();
		if (applicationConf != null) {
			for (Config componentConf : applicationConf.getConfigList(COMPONENT_LIST)) {
				String type = componentConf.getString(COMPONENT_TYPE);
				if (type != null) {
					map.put(type, componentConf);
				}
			}
		}
		return map;
	}

	private Config getApplicationConf() {
		File applicationConfFile = new File(configDirectory, applicationConf);
		if (applicationConfFile.exists()) {
			return ConfigFactory.parseFile(applicationConfFile);
		} else {
			return null;
		}
	}

	public static final class NumberBinder {

		public void apply(ConfigValue configValue) {

		}

	}

}
