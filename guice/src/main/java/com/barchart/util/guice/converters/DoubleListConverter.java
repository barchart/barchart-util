package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class DoubleListConverter extends AbstractListConverter<Double> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}

	@Override
	protected Double convert(Object o) {
		return ((Number) o).doubleValue();
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<Double> list) {
		binder.bind(new TypeLiteral<List<Double>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}
