package com.barchart.util.guice;

import com.google.inject.Injector;

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

}
