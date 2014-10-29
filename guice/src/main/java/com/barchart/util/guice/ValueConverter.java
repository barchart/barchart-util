package com.barchart.util.guice;

import com.google.inject.Binder;
import com.typesafe.config.ConfigValue;

public interface ValueConverter {

	public void applyBindings(Binder binder, String key, ConfigValue value);

}
