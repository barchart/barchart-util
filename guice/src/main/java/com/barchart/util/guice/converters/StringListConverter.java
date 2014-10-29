package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public final class StringListConverter extends AbstractListConverter<String> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return String.class.isAssignableFrom(clazz);
	}

	@Override
	protected String convert(Object o) {
		return (String) o;
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<String> list) {
		binder.bind(new TypeLiteral<List<String>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}
