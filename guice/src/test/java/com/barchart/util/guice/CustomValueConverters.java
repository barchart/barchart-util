package com.barchart.util.guice;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public class CustomValueConverters {

	@Test
	public void testCustomValueConverter() throws Exception {
		Injector injector = GuiceConfigBuilder.create().setDirectory("src/test/resources/CustomValueConverters").build();
		MyValue myValue1 = injector.getInstance(Key.get(MyValue.class, Names.named("test1")));
		MyValue myValue2 = injector.getInstance(Key.get(MyValue.class, Names.named("test2")));
		assertEquals("value1", myValue1.str);
		assertEquals("value2", myValue2.str);
	}

	public static final class MyValue {

		private final String str;

		public MyValue(String str) {
			this.str = str;
		}
	}

	public static final class MyValueConverter implements ValueConverter {

		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.STRING ? new MyValue((String) value.unwrapped()) : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(MyValue.class);
		}

	}
}
