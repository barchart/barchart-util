package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class FloatListConverter extends AbstractListConverter<Float> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}

	@Override
	protected Float convert(Object o) {
		return ((Number) o).floatValue();
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<Float> list) {
		binder.bind(new TypeLiteral<List<Float>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}
