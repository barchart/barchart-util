package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.name.Names;

public class PrivateModuleTest {

	public static final class TopLevel {

		@Inject
		private Component1 component1;

		@Inject
		private Component2 component2;

		@Override
		public String toString() {
			return "TopLevel [component1=" + component1 + ", component2=" + component2 + "]";
		}

	}

	public static final class Component1 {
		@Inject
		@Named("#testfield")
		private String testfield;

		@Inject
		private Component2 component2;

		@Override
		public String toString() {
			return "Component1 [testfield=" + testfield + ", component2=" + component2 + "]";
		}

	}

	public static final class Component2 {
		@Inject
		@Named("#testfield")
		private String testfield;

		@Override
		public String toString() {
			return "Component2 [testfield=" + testfield + "]";
		}
	}

	@Test
	public void testPrivateModules() {
		List<Module> privateModules = new ArrayList<Module>();
		privateModules.add(new PrivateModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("#testfield")).toInstance("hello1");
				bind(Component1.class);
				expose(Component1.class);
			}
		});

		privateModules.add(new PrivateModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("#testfield")).toInstance("hello2");
				bind(Component2.class);
				expose(Component2.class);
			}
		});

		Injector injector = Guice.createInjector(privateModules);
		TopLevel topLevel = injector.getInstance(TopLevel.class);
		assertEquals(topLevel.component1.testfield, "hello1");
		assertEquals(topLevel.component1.component2.testfield, "hello2");
		assertEquals(topLevel.component2.testfield, "hello2");
	}

}
