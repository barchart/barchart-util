package com.barchart.util.test.guice;

import java.util.HashMap;
import java.util.Map;

import com.barchart.util.guice.Filetypes;
import com.barchart.util.guice.GuiceConfigBuilder;
import com.barchart.util.guice.StringResources;
import com.google.inject.Injector;
import com.google.inject.Module;

public class TestInjectorBuilder {

	private GuiceConfigBuilder gcb;
	private Map<String, String> resources;

	private int configs = 1;
	private int components = 1;

	private TestInjectorBuilder(final boolean components) {
		gcb = components ? GuiceConfigBuilder.create() : GuiceConfigBuilder.createBasic();
		resources = new HashMap<String, String>();
	}

	public static TestInjectorBuilder createDefault() {
		return new TestInjectorBuilder(true);
	}

	public static TestInjectorBuilder createBasic() {
		return new TestInjectorBuilder(false);
	}

	public TestInjectorBuilder config(final String config) {
		if (resources.containsKey(Filetypes.DEFAULT_CONFIG_FILE))
			resources.put("/config" + (configs++) + ".conf", config);
		else
			resources.put("/" + Filetypes.DEFAULT_CONFIG_FILE, config);
		return this;
	}

	public TestInjectorBuilder config(final String file, final String config) {
		resources.put(file, config);
		return this;
	}

	public TestInjectorBuilder component(final String config) {
		config("/component" + (components++) + ".component", config);
		return this;
	}

	public TestInjectorBuilder module(final Module module) {
		gcb.addModule(module);
		return this;
	}

	public Injector build() throws Exception {
		return gcb.setConfigResources(new StringResources(resources)).build();
	}

}
