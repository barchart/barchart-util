package com.barchart.util.guice;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

final class ComponentScope implements Scope {

	private static final Logger logger = LoggerFactory.getLogger(ComponentScope.class);

	private Deque<ObjectMap> currentDeque;
	
	ComponentScope() {
		this.currentDeque = new LinkedList<ObjectMap>();
	}

	@Override
	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {

		processKey(key);

		return new Provider<T>() {
			@Override
			public T get() {
				ObjectMap current = currentDeque.peek();
				
				if (current == null) {
					throw new RuntimeException("Could not return instance for " + key + ".  Not in a component scope.");
				}
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
		// TypeLiteral<T> typeLiteral = key.getTypeLiteral();
		// Class<? super T> rawType = typeLiteral.getRawType();
		// ComponentScoped annotation =
		// rawType.getAnnotation(ComponentScoped.class);
		// if (annotation == null) {
		// throw new RuntimeException("No @ComponentScoped annotation for key: "
		// + key);
		// }
	}

	public void enter() {
		currentDeque.push(new ObjectMap());
	}

	public void leave() {
		currentDeque.pop();
	}

	private static final class ObjectMap {

		private Map<Key<?>, Object> store = new HashMap<Key<?>, Object>();

		@SuppressWarnings("unchecked")
		public <T> T get(Key<T> key) {
			return (T) store.get(key);
		}

		public <T> void set(Key<T> key, T instance) {
			store.put(key, instance);
		}

	}

}
