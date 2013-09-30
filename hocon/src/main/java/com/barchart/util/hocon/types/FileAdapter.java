package com.barchart.util.hocon.types;

import java.io.File;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class FileAdapter implements TypeAdapter<File> {

	@Override
	public File convertValue(ConfigValue configValue) {
		return new File(((String) configValue.unwrapped()));
	}

}
