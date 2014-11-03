package com.barchart.util.guice;

import com.google.inject.TypeLiteral;
import com.typesafe.config.ConfigValue;

public interface ValueConverter {

	public Object convert(ConfigValue value);

	public TypeLiteral<?> getBindingType();
	
}
