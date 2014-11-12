package com.barchart.util.guice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.TypeLiteral;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public abstract class ParameterizedValueConverter implements ValueConverter {

	private final TypeLiteral<?> bindingType;

	private Pattern pattern;

	protected ParameterizedValueConverter(TypeLiteral<?> bindingType, String name) {
		this.bindingType = bindingType;
		this.pattern = Pattern.compile(name + "\\((.*)\\)");
	}

	@Override
	public final Object convert(ConfigValue value) {
		return value.valueType() == ConfigValueType.STRING ? convert((String) value.unwrapped()) : null;
	}

	private final Object convert(String str) {
		try {
			Matcher matcher = pattern.matcher(str);
			return matcher.find() ? convertParameter(matcher.group(1)) : null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final TypeLiteral<?> getBindingType() {
		return bindingType;
	}

	protected abstract Object convertParameter(String parameter) throws Exception;
}
