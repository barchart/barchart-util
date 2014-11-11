package com.barchart.util.guice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class PrivateComponentScope implements Scope {

	private static final Logger logger = LoggerFactory.getLogger(PrivateComponentScope.class);

	private final String componentType;

	private final String componentName;

	private Object instance;

	private final GuiceComponentScope guiceComponentScope;


	PrivateComponentScope(GuiceComponentScope guiceComponentScope, String type, String name) {
		logger.info("PrivateComponentScope(): " + type + ", " + name);
		this.guiceComponentScope = guiceComponentScope;
		this.componentType = type;
		this.componentName = name;
	}

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
		return new Provider<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T get() {
				logger.info("Get instance: " + key);
				if (instance == null) {
					logger.info("Create instance...");
					guiceComponentScope.push();
					instance = unscoped.get();
					guiceComponentScope.pop();
					logger.info("Done creating instance...");
				}
				return (T) instance;
			}

		};

	}

}
