package com.barchart.util.guice.component;

import java.io.File;

import com.barchart.util.guice.ValueConverter;
import com.google.inject.Binder;
import com.typesafe.config.ConfigValue;

final class ConfigField {

	private final File file;

	private final String key;

	private final ConfigValue value;

	public ConfigField(File file, String key, ConfigValue value) {
		this.file = file;
		this.key = key;
		this.value = value;
	}

	public void applyBinding(Binder binder, ValueConverter valueConverter) {
	}

	public File getFile() {
		return file;
	}

	public String getKey() {
		return key;
	}

	public ConfigValue getValue() {
		return value;
	}

}
