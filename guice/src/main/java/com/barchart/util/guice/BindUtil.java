package com.barchart.util.guice;

import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;

/**
 * Have to do a lot of unchecked casting. Put the ugliness here.
 */
final class BindUtil {

	private final Binder binder;

	public BindUtil(Binder binder) {
		this.binder = binder;
	}

	public void bindInstance(Class<?> bindingType, String name, Object instance) {
		@SuppressWarnings("unchecked")
		AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) binder.bind(bindingType);
		bindingBuilder.annotatedWith(Names.named(name)).toInstance(instance);
	}

	public void bindInstance(TypeLiteral<?> bindingType, String name, Object instance) {
		@SuppressWarnings("unchecked")
		AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) binder.bind(bindingType);
		bindingBuilder.annotatedWith(Names.named(name)).toInstance(instance);
	}

}
