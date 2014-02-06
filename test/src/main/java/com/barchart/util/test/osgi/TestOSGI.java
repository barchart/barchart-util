package com.barchart.util.test.osgi;

import com.barchart.conf.util.BaseComponent;

public class TestOSGI {

	public static <C extends BaseComponent> ComponentInitializer<C> component(final C component) {
		return new ComponentInitializer<C>(component);
	}

	public static <C extends BaseComponent> ComponentInitializer<C> component(final Class<C> type) {
		return new ComponentInitializer<C>(type);
	}

}
