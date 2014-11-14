package com.barchart.util.guice;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

public class ComponentGenericBindings {

	private Injector injector;

	private String getApplicationConfString() {
		StringBuilder b = new StringBuilder();
		b.append("component = [\n");
		b.append("  {\n");
		b.append("    type = \"ComponentGenericBinding.MyComponent\"\n");
		b.append("  }\n");
		b.append("]");
		return b.toString();
	}

	@Before
	public void setup() throws Exception {
		ConfigResources stringResources = new StringResources(getApplicationConfString());
		this.injector = GuiceConfigBuilder.create() //
				.setConfigResources(stringResources) //
				.build();
	}

	@Test
	public void testBindingToGenericInterface() {
		Base base = injector.getInstance(Base.class);
		assertEquals("test", base.generic.get());
	}

	public interface GenericInterface<T> {
		public T get();
	}

	public static final class Base {

		@Inject
		private GenericInterface<String> generic;
	}

	@Component("ComponentGenericBinding.MyComponent")
	public static final class MyComponent implements GenericInterface<String> {

		@Override
		public String get() {
			return "test";
		}

	}

}
