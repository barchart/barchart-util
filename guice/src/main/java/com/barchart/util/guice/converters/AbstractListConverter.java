package com.barchart.util.guice.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.barchart.util.guice.ValueConverter;
import com.google.inject.Binder;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public abstract class AbstractListConverter<T> implements ValueConverter {

	@SuppressWarnings("unchecked")
	@Override
	public final void applyBindings(Binder binder, String key, ConfigValue value) {
		if (value.valueType() == ConfigValueType.LIST) {
			List<Object> unwrapped = (List<Object>) value.unwrapped();
			if (unwrapped.isEmpty() || isCastable(unwrapped.get(0).getClass())) {
				applyBinding(binder, key, buildList(unwrapped));
			}
		}
	}

	protected abstract void applyBinding(Binder binder, String key, List<T> list);

	protected List<T> buildList(List<Object> unwrapped) {
		List<T> list = new ArrayList<T>();
		for (Object o : unwrapped) {
			list.add(convert(o));
		}
		return Collections.unmodifiableList(list);
	}

	protected abstract T convert(Object o);

	protected abstract boolean isCastable(Class<?> clazz);

}
