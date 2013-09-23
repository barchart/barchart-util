package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public class StringAdapter implements TypeAdapter<String> {

//	@Override
//	public String convertValue(Config config, String path) {
//		return config.getString(path);
//	}

	@Override
	public String convertValue(ConfigValue configValue) {
		return ((String) configValue.unwrapped());
	}

}
