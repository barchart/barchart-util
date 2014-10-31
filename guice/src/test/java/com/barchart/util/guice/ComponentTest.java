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

	private static final class InjectComponent1 extends TestCase {
		@Inject
		@Named("comp1")
		private Component1 component1;

		@Override
		void test() {
			assertEquals("comp1", component1.name);
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

	private static final class BindByInterface extends TestCase {
		
		@Inject
		@Named("comp2")
		private ComponentInterface component1;

		@Inject
		@Named("comp1")
		private ComponentInterface component2;
		
		@Override
		void test() {
			// TODO Auto-generated method stub
			
		}
	}

	@Test
	public void testComponent1Injection() {
		runTest(InjectComponent1.class);
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
