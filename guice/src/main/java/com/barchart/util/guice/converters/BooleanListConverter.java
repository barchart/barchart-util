package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class BooleanListConverter extends AbstractListConverter<Boolean> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return Boolean.class.isAssignableFrom(clazz);
	}

	@Override
	protected Boolean convert(Object o) {
		return (Boolean) o;
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<Boolean> list) {
		binder.bind(new TypeLiteral<List<Boolean>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}
