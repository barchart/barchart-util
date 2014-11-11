package com.barchart.util.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.TypeLiteral;

final class GuiceComponentScope implements Scope {

	private static final Logger logger = LoggerFactory.getLogger(GuiceComponentScope.class);

	private ObjectMap current;

	GuiceComponentScope() {
		logger.info("GuiceComponentScope()");

	}

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {

		processKey(key);

		return new Provider<T>() {
			@Override
			public T get() {
				logger.error("Get Guice component scope key=" + key);
				T instance = current.get(key);
				if (instance == null) {
					instance = unscoped.get();
					current.set(key, instance);
				}
				return instance;

			}
		};
	}

	private <T> void processKey(Key<T> key) {
		TypeLiteral<T> typeLiteral = key.getTypeLiteral();
		Class<? super T> rawType = typeLiteral.getRawType();
		ComponentScoped annotation = rawType.getAnnotation(ComponentScoped.class);
		if (annotation == null) {
			throw new RuntimeException("No @ComponentScoped annotation for key: " + key);
		}
		logger.info("Component scoped....: " + key + ". scoped to type: " + annotation.type() + ", name: " + annotation.name());
		
		
	}

	public void push() {
		logger.info("Push");
		this.current = new ObjectMap();
	}

	public void pop() {

	}

}
