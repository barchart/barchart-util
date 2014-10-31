package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
//import javax.inject.Named;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

public class ComponentTest {

	private static final Logger logger = LoggerFactory.getLogger(ComponentTest.class);

	public static interface ComponentInterface {
		public String getName();
	}

	@Component("component1")
	public static final class Component1 implements ComponentInterface {

		@Inject
		@Named("#name")
		private String name;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "Component1 [name=" + name + "]";
		}

	}

	@Component("component2")
	public static final class Component2 implements ComponentInterface {

		@Inject
		@Named("#name")
		private String name;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return "Component2 [name=" + name + "]";
		}

	}

	private static final class Component1Injection extends TestCase {
		@Inject
		@Named("comp1")
		private Component1 component1;

		@Override
		void test() {
			assertEquals("comp1", component1.name);
		}
	}

	@Test
	public void testComponent1Injection() {
		runTest(Component1Injection.class);
	}

	private Injector injector;

	@Before
	public void setup() {
		this.injector = GuiceConfigBuilder.create() //
				.setDirectory("src/test/resources/componenttest") //
				.build();
	}

	private <T> T get(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	private void runTest(Class<? extends TestCase> clazz) {
		get(clazz).test();
	}

}
