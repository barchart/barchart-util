//package com.barchart.util.guice;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.inject.Key;
//import com.google.inject.Provider;
//import com.google.inject.Scope;
//import com.google.inject.Scopes;
//
//public class ActualComponentScope implements Scope {
//
//	private static final Logger logger = LoggerFactory.getLogger(ActualComponentScope.class);
//
//	private final GuiceComponentScope guiceComponentScope;
//
//	private final ObjectMap objectMap;
//
//	public ActualComponentScope(GuiceComponentScope guiceComponentScope) {
//		this.guiceComponentScope = guiceComponentScope;
//		this.objectMap = new ObjectMap();
//	}
//
//	@Override
//	public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped) {
//		return Scopes.SINGLETON.scope(key, new Provider<T>() {
//			@Override
//			public T get() {
//				guiceComponentScope.enter(new ObjectMap());
//				return unscoped.get();
//			}
//		});
//	}
//
////	@Override
////	public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
////		logger.error("Key: " + key + ", provider: " + unscoped);
////		return new Provider<T>() {
////			@Override
////			public T get() {
////				T instance = objectMap.get(key);
////				logger.error("Get() for key " + key + ", instance: " + instance);
////				if (instance == null) {
////					guiceComponentScope.enter(new ObjectMap());
////					instance = unscoped.get();
////					objectMap.set(key, instance);
////				}
////				return instance;
////
////			}
////		};
////	}
//
//}
