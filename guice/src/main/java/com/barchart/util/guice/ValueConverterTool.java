package com.barchart.util.guice;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public class ValueConverterTool {

	private static final Logger logger = LoggerFactory.getLogger(ValueConverterTool.class);

	@Inject
	private Set<ValueConverter> valueConverters;

	ValueConverterTool() {
	}

	public Map<TypeLiteral<?>, Object> getConversions(ConfigValue value) {
		Map<TypeLiteral<?>, Object> map = new HashMap<TypeLiteral<?>, Object>();
		if (value.valueType() == ConfigValueType.LIST) {
			handleGenericListConversion((ConfigList) value, map);
		}
		for (ValueConverter converter : valueConverters) {
			Object result = converter.convert(value);
			if (result != null) {
				populateMap(map, converter.getBindingType(), value, result);
			}
		}
		return map;
	}

	private void handleGenericListConversion(ConfigList value, Map<TypeLiteral<?>, Object> map) {
		List<Object> unwrapped = value.unwrapped();
		for (ValueConverter converter : valueConverters) {
			List<Object> result = convertStringBasedList(converter, unwrapped);
			if (result != null) {
				TypeLiteral<?> bindingType = TypeLiteral.get(Types.listOf(converter.getBindingType().getType()));
				populateMap(map, bindingType, value, result);
			}
		}
	}

	private void populateMap(Map<TypeLiteral<?>, Object> map, TypeLiteral<?> bindingType, ConfigValue value, Object result) {
		if (!map.containsKey(bindingType)) {
			map.put(bindingType, result);
		} else {
			logger.warn("Value  " + value + " has multiple conversions for type " + bindingType);
		}
	}

	private List<Object> convertStringBasedList(ValueConverter converter, List<Object> unwrapped) {
		ArrayList<Object> result = new ArrayList<Object>();
		for (Object input : unwrapped) {
			Object convertedValue = convertStringValue(converter, input);
			if (convertedValue != null) {
				result.add(convertedValue);
			} else {
				return null;
			}
		}
		return result;
	}

	private Object convertStringValue(ValueConverter converter, Object input) {
		ConfigValue value = getStringValue(input);
		if (value != null) {
			return converter.convert(value);
		} else {
			return null;
		}
	}

	private ConfigValue getStringValue(Object input) {
		if (input instanceof String) {
			return ConfigFactory.parseString("tmp = \"" + input + "\"").getValue("tmp");
		} else {
			return null;
		}
	}

}
