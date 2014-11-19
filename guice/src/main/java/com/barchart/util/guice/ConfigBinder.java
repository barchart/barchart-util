package com.barchart.util.guice;

import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public class ConfigBinder {

	private final ValueConverterTool valueConverterTool;

	private Binder binder;

	public ConfigBinder(Binder binder, ValueConverterTool valueConverterTool) {
		this.binder = binder;
		this.valueConverterTool = valueConverterTool;
	}

	public void applyBindings(Config config) {
		applyBindings(config, "");
	}

	public void applyBindings(Config config, String prefix) {
		Applicator applicator = new Applicator(config, prefix);
		applicator.apply();
	}

	private final class Applicator {

		private final Config config;

		private final String prefix;

		public Applicator(Config config, String prefix) {
			this.config = config;
			this.prefix = prefix;
		}

		public void apply() {
			BindUtil bindUtil = new BindUtil(binder);
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
				bindUtil.bindInstance(bindingType, prefix + key, result);
			}
		}

		private void bindConfigObjectPaths(BindUtil bindUtil, UniqueObjectPathSet objectPaths) {
			bindConfigValue(bindUtil, "", config.root());

			for (String objectPath : objectPaths) {
				Config object = config.getConfig(objectPath);
				bindConfigValue(bindUtil, objectPath, object.root());
			}
		}

	}
}