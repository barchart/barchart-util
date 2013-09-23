package com.barchart.util.hocon.types;

import com.barchart.util.hocon.TypeAdapter;
import com.typesafe.config.ConfigValue;

public class ByteAdapter implements TypeAdapter<Byte> {

	@Override
	public Byte convertValue(ConfigValue configValue) {
		return ((Integer) configValue.unwrapped()).byteValue();
	}

}
