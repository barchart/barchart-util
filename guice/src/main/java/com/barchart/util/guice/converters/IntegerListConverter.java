package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class IntegerListConverter extends AbstractListConverter<Integer> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}

	@Override
	protected Integer convert(Object o) {
		return ((Number) o).intValue();
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<Integer> list) {
		binder.bind(new TypeLiteral<List<Integer>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}
