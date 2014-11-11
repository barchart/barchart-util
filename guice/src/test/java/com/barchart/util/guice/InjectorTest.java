package com.barchart.util.guice;

import java.lang.annotation.Annotation;

import com.google.inject.Injector;
import com.google.inject.Key;

public class InjectorTest {

	private Injector injector;

	InjectorTest() {

	}

	public void setup(String directory) throws Exception {
		this.injector = GuiceConfigBuilder.create() //
				.setDirectory(directory) //
				.build();
	}

	protected <T> T get(Class<T> clazz) {
		return injector.getInstance(clazz);
	}
	
	protected <T> T get(Class<T> clazz, Annotation annoation) {
		return injector.getInstance(Key.get(clazz, annoation));
	}
	
	protected <T> T getInstance(Key<T> key) {
		return injector.getInstance(key);
	}

}
