package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class ShortListConverter extends AbstractListConverter<Short> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}

	@Override
	protected Short convert(Object o) {
		return ((Number) o).shortValue();
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<Short> list) {
		binder.bind(new TypeLiteral<List<Short>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}