package com.barchart.util.guice;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class ComponentScopeTest {

	private static final Logger logger = LoggerFactory.getLogger(ComponentScopeTest.class);

	private Injector injector;

	@Before
	public void setup() throws Exception {
		injector = GuiceConfigBuilder.create().setDirectory("src/test/resources/ComponentScopeTest").build();
	}

	@Test
	public void nonScopedSingletonDependenciesAreShared() throws Exception {
		TestComponent comp1 = injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));
		TestComponent comp2 = injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));
		logger.info("Comp1: " + comp1.sharedDep + ", comp2: " + comp2.sharedDep);
		assertSame(comp1.sharedDep, comp2.sharedDep);
	}

	@Test
	public void componentScopedDependenciesAreNotShared() {
		logger.info("\n\nGet comp1");
		TestComponent comp1 = injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));
		
		logger.info("\n\nGet comp2");
		TestComponent comp2 = injector.getInstance(Key.get(TestComponent.class, Names.named("comp2")));
		logger.info("Comp1: " + comp1.componentDep + ", comp2: " + comp2.componentDep);
		assertNotSame(comp1.componentDep, comp2.componentDep);
	}
	
	@Test
	public void componentsAreTheSameInstance() {
		TestComponent comp1A = injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));
		TestComponent comp1B = injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));
		TestComponent comp1C = injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));
		TestComponent comp1D = injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));

		
		TestComponent comp2A = injector.getInstance(Key.get(TestComponent.class, Names.named("comp2")));
		TestComponent comp2B = injector.getInstance(Key.get(TestComponent.class, Names.named("comp2")));
		TestComponent comp2C = injector.getInstance(Key.get(TestComponent.class, Names.named("comp2")));
		TestComponent comp2D = injector.getInstance(Key.get(TestComponent.class, Names.named("comp2")));
		
		Object obj = injector.getInstance(Key.get(Object.class, Names.named("comp1")));
		
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
		TestComponent c1= injector.getInstance(Key.get(TestComponent.class, Names.named("comp1")));
		
		logger.info("\n\n");
		logger.info("Get TestComponent");
		TestComponent c2= injector.getInstance(Key.get(TestComponent.class, Names.named("comp2")));
		
		logger.info("\n\n");
		logger.info("Get TestComponent");
		IFace c3 = injector.getInstance(Key.get(IFace.class, Names.named("comp1")));
		

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


interface IFace {
	
}

@Component(value = "ComponentScopeTest.component")
class TestComponent implements IFace {
	@Inject
	public SharedDependency sharedDep;
	
	@Inject
	public ComponentScopedDependency componentDep;
}

@Singleton
class SharedDependency {

}

@ComponentScoped
class ComponentScopedDependency {
	
}