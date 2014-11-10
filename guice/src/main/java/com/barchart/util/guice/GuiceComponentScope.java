package com.barchart.util.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

final class GuiceComponentScope implements Scope {

	private static final Logger logger = LoggerFactory.getLogger(GuiceComponentScope.class);
	
	private ObjectMap current;

	GuiceComponentScope() {
		logger.info("GuiceComponentScope()");
	}

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
		logger.error("Scope inside component. key=" + key);
		return new Provider<T>() {
			@Override
			public T get() {
				T instance = current.get(key);
				if (instance == null) {
					instance = unscoped.get();
					current.set(key, instance);
				}
				return instance;

			}
		};
	}

	public void enter(ObjectMap scope) {
		logger.error("Enter: " +scope);
		current = scope;
	}

	public void leave() {
		current = null;
	}

}
