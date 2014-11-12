package com.barchart.util.guice;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;


import com.barchart.util.guice.Common.*;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

@RunWith(Enclosed.class)
public class ComponentScopeTest {

	private static final Logger logger = LoggerFactory.getLogger(ComponentScopeTest.class);

	
	/*
	 * 
	 */
	public static class TestCase1 extends InjectorTest {
		
		@Before
		public void setup() throws Exception {
			super.setup("src/test/resources/ComponentScopeTest/TestCase1");
		}

		@Test
		public void nonScopedSingletonDependenciesAreShared() throws Exception {
			Common.TestComponent comp1 = get(Common.TestComponent.class, Names.named("comp1"));
			Common.TestComponent comp2 = get(Common.TestComponent.class, Names.named("comp1"));
			assertSame(comp1.sharedDep, comp2.sharedDep);
		}
		
		
		@Test
		public void componentScopedDependenciesAreNotShared() {
			logger.info("\n\nGet comp1");
			TestComponent comp1 = getInstance(Key.get(TestComponent.class, Names.named("comp1")));
			
			logger.info("\n\nGet comp2");
			TestComponent comp2 = getInstance(Key.get(TestComponent.class, Names.named("comp2")));
			logger.info("Comp1: " + comp1.componentDep + ", comp2: " + comp2.componentDep);
			assertNotSame(comp1.componentDep, comp2.componentDep);
		}
		
		@Test
		public void componentsAreTheSameInstance() {
			TestComponent comp1A = getInstance(Key.get(TestComponent.class, Names.named("comp1")));
			TestComponent comp1B = getInstance(Key.get(TestComponent.class, Names.named("comp1")));
			TestComponent comp1C = getInstance(Key.get(TestComponent.class, Names.named("comp1")));
			TestComponent comp1D = getInstance(Key.get(TestComponent.class, Names.named("comp1")));

			
			TestComponent comp2A = getInstance(Key.get(TestComponent.class, Names.named("comp2")));
			TestComponent comp2B = getInstance(Key.get(TestComponent.class, Names.named("comp2")));
			TestComponent comp2C = getInstance(Key.get(TestComponent.class, Names.named("comp2")));
			TestComponent comp2D = getInstance(Key.get(TestComponent.class, Names.named("comp2")));
			
			Object obj = getInstance(Key.get(Object.class, Names.named("comp1")));
			
			logger.info("Obj : " + obj.hashCode() + " comp1: " + comp1A.hashCode());
			
			assertSame(comp1A, comp1B);
			assertSame(comp1A, comp1C);
			assertSame(comp1A, comp1D);
			
			assertSame(comp2A, comp2B);
			assertSame(comp2A, comp2C);
			assertSame(comp2A, comp2D);
			
		}
		
		@Test
		public void scopeByInterface() {
			logger.info("Done configuring injector");
			
			
			logger.info("\n\n");
			logger.info("Get TestComponent");
			TestComponent c1= getInstance(Key.get(TestComponent.class, Names.named("comp1")));
			
			logger.info("\n\n");
			logger.info("Get TestComponent");
			TestComponent c2= getInstance(Key.get(TestComponent.class, Names.named("comp2")));
			
			logger.info("\n\n");
			logger.info("Get TestComponent");
			IFace c3 = getInstance(Key.get(IFace.class, Names.named("comp1")));
			

		}
		
		public static class TestCase {
			@Inject
			@Named("comp1")
			public IFace c1;
			
			@Inject
			@Named("comp2")
			public IFace c2;
		}

	}
	
	/*
	 * 
	 * 	[Component1]        [Component2]
	 *    |     |              |    |
	 *    |     |              |    |
   	 *   dep1  dep1           dep2 dep2
   	 *   
   	 * dep1 and dep2 are different
   	 *           
	 */
	public static class TestCase10 extends InjectorTest {

		private Root root;

		@Before
		public void setup() throws Exception {
			super.setup("src/test/resources/ComponentScopeTest/Scopes");
			this.root = get(Root.class);
		}

		@Test
		public void componentsHaveDifferentDependencies() {
			Dependency dep1 = root.component1.dependencyA;
			Dependency dep2 = root.component2.dependencyA;
			assertNotSame(dep1, dep2);
		}
		
		@Test
		public void sameComponentHasSameDependency() {
			Dependency dep1A = root.component1.dependencyA;
			Dependency dep1B = root.component1.dependencyB;
			assertSame(dep1A, dep1B);
			
			Dependency dep2A = root.component2.dependencyA;
			Dependency dep2B = root.component2.dependencyB;
			assertSame(dep2A, dep2B);
		}

		private static class Root {

			@Inject
			@Named("comp1")
			public TestComponent component1;

			@Inject
			@Named("comp2")
			public TestComponent component2;

		}

		@Component("ScopeTest.TestComponent")
		private static class TestComponent {

			@Inject
			public Dependency dependencyA;
			
			@Inject
			public Dependency dependencyB;

		}

		@ComponentScoped
		private static class Dependency {

		}

	}
	
	



}


