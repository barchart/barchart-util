package com.barchart.util.test.guice;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.guice.Activate;
import com.barchart.util.guice.Component;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

public class TestTestInjectorBuilder {

	@Test
	public void testGlobalConfig() throws Exception {

		final SimpleModule sm = TestInjectorBuilder.create()
			.config("{ root = value }")
			.build()
			.getInstance(SimpleModule.class);

		assertEquals("value", sm.rootValue);
		assertEquals("value", sm.rootConfig.getString("root"));

	}

	@Test
	public void testComponentConfig() throws Exception {

		final SimpleModule sm = TestInjectorBuilder.create()
				.component(SimpleModule.class, "{ name = simple }")
				.build()
				.getInstance(SimpleModule.class);

		assertEquals("simple", sm.componentName);
		assertEquals("simple", sm.componentConfig.getString("name"));

	}

	@Test
	public void testComponentActivation() throws Exception {

		final SimpleModule sm = TestInjectorBuilder.create()
				.component(SimpleModule.class, "{ name = simple }")
				.build()
				.getInstance(SimpleModule.class);

		assertTrue(sm.activated);

	}

	@Component("simple.module")
	public static class SimpleModule extends AbstractModule {

		@Inject(optional = true)
		@Named("/")
		private Config rootConfig;

		@Inject(optional = true)
		@Named("root")
		private String rootValue;

		@Inject(optional = true)
		@Named("#")
		private Config componentConfig;

		@Inject(optional = true)
		@Named("#name")
		private String componentName;

		private boolean activated = false;

		@Override
		protected void configure() {

		}

		@Activate
		private void activate() {
			activated = true;
		}

	}

}
