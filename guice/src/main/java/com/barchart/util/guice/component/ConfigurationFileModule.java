package com.barchart.util.guice.component;

import java.io.File;

import com.google.inject.Binder;
import com.google.inject.Module;

public class ConfigurationFileModule implements Module {

	public ConfigurationFileModule(File file) {
	}

	@Override
	public void configure(Binder binder) {
		// TODO Auto-generated method stub

	}

}
