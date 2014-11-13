package com.barchart.util.guice;

import static org.junit.Assert.*;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;

public class EmptyMultibindingsTest extends InjectorTest {

	@Before
	public void setup() throws Exception {
		super.setup("src/test/resources/EmptyMultibindingsTest");
	}
	
	@Test
	public void emptySetForComponentWithNoConfiguration() {
		Key<Set<MyComponent>> key = Key.get(new TypeLiteral<Set<MyComponent>>() {
		});
		Set<MyComponent> set = getInstance(key);
		assertTrue(set.isEmpty());
	}
	
	@Test
	public void emptySetForComponentSuperclass() {
		Key<Set<AbstractComponent>> key = Key.get(new TypeLiteral<Set<AbstractComponent>>() {
		});
		Set<AbstractComponent> set = getInstance(key);
		assertTrue(set.isEmpty());		
	}

	@Test
	public void emptySetForComponentInterface() {
		Key<Set<ComponentInterface>> key = Key.get(new TypeLiteral<Set<ComponentInterface>>() {
		});
		Set<ComponentInterface> set = getInstance(key);
		assertTrue(set.isEmpty());		
	}

	private static interface ComponentInterface {
		
	}
	
	private static abstract class AbstractComponent {
		
	}
	
	@Component("EmptyMultibindingsTest.MyComponent")
	private static final class MyComponent extends AbstractComponent implements ComponentInterface {
		@Inject
		@Named("#name")
		private String name;
	}

}
