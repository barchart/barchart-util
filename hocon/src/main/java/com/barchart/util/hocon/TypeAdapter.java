package com.barchart.util.hocon;

import com.typesafe.config.ConfigValue;

public interface TypeAdapter<T> {
	
	public T convertValue(ConfigValue configValue);
	
	
}
