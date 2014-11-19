package com.barchart.util.guice;

import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public class ConfigBinder {

	private final Config config;

	private final String simpleFilename;

	private final ValueConverterTool valueConverterTool;

	public ConfigBinder(Config config, ValueConverterTool valueConverterTool) {
		this.config = config;
		this.simpleFilename = Filetypes.getSimpleName(config);
		this.valueConverterTool = valueConverterTool;
	}

	public void applyBindings(Binder binder) {
		BindUtil bindUtil = new BindUtil(binder);
		if (Filetypes.isDefaultConfigFile(config)) {
			binder.bind(Config.class).toInstance(config);
			binder.bind(Config.class).annotatedWith(Names.named("/")).toInstance(config);
		}

		UniqueObjectPathSet objectPaths = new UniqueObjectPathSet();
		for (Entry<String, ConfigValue> entry : config.entrySet()) {
			String configValuePath = entry.getKey();
			objectPaths.add(configValuePath);
			bindConfigValue(bindUtil, configValuePath, entry.getValue());
		}
		bindConfigObjectPaths(bindUtil, objectPaths);
	}

	private void bindConfigValue(BindUtil bindUtil, String key, ConfigValue value) {

		for (Map.Entry<TypeLiteral<?>, Object> entry : valueConverterTool.getConversions(value).entrySet()) {
			TypeLiteral<?> bindingType = entry.getKey();
			Object result = entry.getValue();
			bindUtil.bindInstance(bindingType, getPath(key), result);
			if (Filetypes.isDefaultConfigFile(config)) {
				bindUtil.bindInstance(bindingType, key, result);
			}
		}
	}

	private void bindConfigObjectPaths(BindUtil bindUtil, UniqueObjectPathSet objectPaths) {
		bindConfigValue(bindUtil, "", config.root());

		for (String objectPath : objectPaths) {
			Config object = config.getConfig(objectPath);
			bindConfigValue(bindUtil, objectPath, object.root());
		}
	}

	private String getPath(String key) {
		return simpleFilename + "/" + key;
	}
}