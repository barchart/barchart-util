package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class LongListConverter extends AbstractListConverter<Long> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}

	@Override
	protected Long convert(Object o) {
		return ((Number) o).longValue();
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<Long> list) {
		binder.bind(new TypeLiteral<List<Long>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}
