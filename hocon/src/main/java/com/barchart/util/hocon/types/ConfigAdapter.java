package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public class ConfigAdapter implements TypeAdapter<Config> {

	@Override
	public Config convertValue(ConfigValue configValue) {
		return configValue.atPath("");
	}

}
