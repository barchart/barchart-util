package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.TypeLiteral;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;

public class ParameterizedValueConverterTest {

	private ParameterizedValueConverter converter;


	@Before
	public void setup() {
		this.converter = new ParameterizedValueConverter(TypeLiteral.get(String.class), "test") {
			@Override
			protected Object convertParameter(String parameter) {
				return parameter;
			}
		};	
	}
	
	
	@Test
	public void test1() {
		assertEquals("abc", converter.convert(value("test(abc)")));
	}
	
	@Test
	public void test2() {
		assertEquals("10, 11, 12, 13", converter.convert(value("test(10, 11, 12, 13)")));
	}


	private ConfigValue value(String valueText) {
		valueText = "\"" + valueText + "\"";
		String configString = "key = " + valueText;
		Config config = ConfigFactory.parseString(configString);
		return config.getValue("key");
	}
	
}
