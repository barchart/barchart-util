package com.barchart.util.guice.converters;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

public class ByteListConverter extends AbstractListConverter<Byte> {

	@Override
	protected boolean isCastable(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz);
	}

	@Override
	protected Byte convert(Object o) {
		return ((Number) o).byteValue();
	}

	@Override
	protected void applyBinding(Binder binder, String key, List<Byte> list) {
		binder.bind(new TypeLiteral<List<Byte>>() {
		}).annotatedWith(Names.named(key)).toInstance(list);
	}

}
