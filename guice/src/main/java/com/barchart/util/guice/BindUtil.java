package com.barchart.util.guice;

import com.barchart.util.guice.encryption.EncryptedAnno;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;

/**
 * Have to do a lot of unchecked casting. Put the ugliness here.
 */
final class BindUtil {

	private final Binder binder;

	public BindUtil(final Binder binder) {
		this.binder = binder;
	}

	public void bindInstance(final Class<?> bindingType, final String name, final Object instance) {
		@SuppressWarnings("unchecked")
		final
		AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) binder.bind(bindingType);
		bindingBuilder.annotatedWith(Names.named(name)).toInstance(instance);
	}

	public void bindInstance(final TypeLiteral<?> bindingType, final String name, final Object instance) {
		@SuppressWarnings("unchecked")
		final
		AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) binder.bind(bindingType);
		bindingBuilder.annotatedWith(Names.named(name)).toInstance(instance);
	}

	public void bindEncrypted(final Class<?> bindingType, final String name, final Object instance) {
		@SuppressWarnings("unchecked")
		final AnnotatedBindingBuilder<Object> bindingBuilder =
				(AnnotatedBindingBuilder<Object>) binder.bind(bindingType);
		bindingBuilder.annotatedWith(EncryptedAnno.named(name)).toInstance(instance);
	}

	public void bindEncrypted(final TypeLiteral<?> bindingType, final String name, final Object instance) {
		@SuppressWarnings("unchecked")
		final AnnotatedBindingBuilder<Object> bindingBuilder =
				(AnnotatedBindingBuilder<Object>) binder.bind(bindingType);
		bindingBuilder.annotatedWith(EncryptedAnno.named(name)).toInstance(instance);
	}

}
