package com.barchart.util.guice;

import com.barchart.util.guice.encryption.Decrypter;
import com.google.inject.AbstractModule;

public class DecrypterStaticModule extends AbstractModule {

	private final Decrypter provided;

	public DecrypterStaticModule(final Decrypter provided_) {
		provided = provided_;
	}

	@Override
	protected void configure() {
		bind(Decrypter.class).toInstance(provided);
	}

}