package com.barchart.util.guice;

import static org.junit.Assert.*;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
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

	
	@Test
	public void testCustomValueList() throws Exception {
		Injector injector = GuiceConfigBuilder.create().setDirectory("src/test/resources/CustomValueConverters").build();
		TypeLiteral<List<MyValue>> typeLiteral = new TypeLiteral<List<MyValue>>() {};
		List<MyValue> customList = injector.getInstance(Key.get(typeLiteral, Names.named("list")));
		assertEquals(2, customList.size());
		assertEquals("listval1", customList.get(0).str);
		assertEquals("listval2", customList.get(1).str);
	}
	
	
	public static final class MyValue {

		private final String str;

		public MyValue(String str) {
			this.str = str;
		}

		@Override
		public String toString() {
			return "MyValue [str=" + str + "]";
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
