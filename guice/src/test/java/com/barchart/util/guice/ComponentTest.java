package com.barchart.util.guice;

import static org.junit.Assert.*;

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

	public static interface Component1Interface {

	}

	public static abstract class AbstractComponent implements ComponentInterface {

	}

	@Component("component1")
	public static final class Component1 extends AbstractComponent implements Component1Interface {

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
	public static final class Component2 extends AbstractComponent {

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

	private static final class InjectComponent1 extends TestCase {
		@Inject
		@Named("comp1")
		private Component1 component1;

		@Override
		void test() {
			assertEquals("comp1", component1.getName());
		}
	}

	private static final class InjectComponent1ByInterface extends TestCase {

		@Inject
		@Named("comp1")
		private Component1Interface component1;

		@Override
		void test() {
			assertEquals("comp1", ((Component1) component1).getName());
		}
	}

	private static final class InjectComponent1BySuperclass extends TestCase {

		@Inject
		@Named("comp1")
		private AbstractComponent component1;

		@Override
		void test() {
			assertEquals("comp1", component1.getName());
		}

	}

	private static final class InjectComponent1BySuperclassesInterface extends TestCase {

		@Inject
		@Named("comp1")
		private ComponentInterface component1;

		@Override
		void test() {
			assertEquals("comp1", component1.getName());
		}
	}

	private static final class InjectComponent1BySuperSuperClass extends TestCase {

		@Inject
		@Named("comp1")
		private Object component1;

		@Override
		void test() {
			assertEquals("comp1", ((Component1) component1).getName());
		}
	}

	private static final class DuplicateComponentInjections extends TestCase {

		@Inject
		@Named("comp1")
		private Component1 instance1;

		@Inject
		@Named("comp1")
		private Component1 instance2;

		@Override
		void test() {
			assertSame(instance1, instance2);
		}
	}

	@Test
	public void testComponent1Injection() {
		runTest(InjectComponent1.class);
	}

	@Test
	public void testInjectComponent1ByInterface() {
		runTest(InjectComponent1ByInterface.class);
	}

	@Test
	public void testInjectComponent1BySuperclass() {
		runTest(InjectComponent1BySuperclass.class);
	}

	@Test
	public void testInjectComponent1BySuperclassesInterface() {
		runTest(InjectComponent1BySuperclassesInterface.class);
	}

	@Test
	public void testInjectComponent1BySuperSuperClass() {
		runTest(InjectComponent1BySuperSuperClass.class);
	}

	@Test
	public void testDuplicateComponentInjections() {
		runTest(DuplicateComponentInjections.class);
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
