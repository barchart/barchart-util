package com.barchart.util.test.guice;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.guice.Activate;
import com.barchart.util.guice.Component;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

public class TestTestInjectorBuilder {

	@Test
	public void testGlobalConfig() throws Exception {

		final SimpleComponent sm = TestInjectorBuilder.createDefault()
				.config("{ root = value, component = [{type = simple.component}]}")
				.build()
				.getInstance(SimpleComponent.class);

		assertEquals("value", sm.rootValue);
		assertEquals("value", sm.rootConfig.getString("root"));

	}

	@Test
	public void testManualComponentConfig() throws Exception {

		final SimpleComponent sm = TestInjectorBuilder.createBasic()
				.module(TestComponentModule.forType(SimpleComponent.class).config("{ name = simple }"))
				.build()
				.getInstance(SimpleComponent.class);

		assertEquals("simple", sm.componentName);
		assertEquals("simple", sm.componentConfig.getString("name"));

	}

	@Test
	public void testManualComponentActivation() throws Exception {

		final SimpleComponent sm = TestInjectorBuilder.createBasic()
				.module(TestComponentModule.forType(SimpleComponent.class).config("{ name = simple }"))
				.build()
				.getInstance(SimpleComponent.class);

		assertTrue(sm.activated);

	}

	@Test
	public void testAutoComponentConfig() throws Exception {

		final SimpleComponent sm = TestInjectorBuilder.createDefault()
				.component("{ type = \"simple.component\", name = \"simple\" }")
				.build()
				.getInstance(SimpleComponent.class);

		assertEquals("value", sm.rootValue);
		assertEquals("value", sm.rootConfig.getString("root"));

		assertEquals("simple", sm.componentName);
		assertEquals("simple", sm.componentConfig.getString("name"));

		assertTrue(sm.activated);

	}

}

@Component("simple.component")
class SimpleComponent {

	@Inject(optional = true)
	@Named("/")
	Config rootConfig;

	@Inject(optional = true)
	@Named("root")
	String rootValue;

	@Inject(optional = true)
	@Named("#")
	Config componentConfig;

	@Inject(optional = true)
	@Named("#name")
	String componentName;

	boolean activated = false;

	@Activate
	private void activate() {
		activated = true;
	}

}
