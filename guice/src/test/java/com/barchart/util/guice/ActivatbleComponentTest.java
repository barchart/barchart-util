package com.barchart.util.guice;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

public class ActivatbleComponentTest extends InjectorTest {

	@Before
	public void setup() throws Exception {
		super.setup("src/test/resources/ActivatbleComponentTest");
	}

	@Test
	public void test() {
		MyComponent myComponent1 = get(MyComponent.class);
		MyComponent myComponent2 = get(MyComponent.class);
		assertTrue(myComponent1.activated);
		assertTrue(myComponent2.activated);
		assertSame(myComponent1, myComponent2);
	}

	@Component(type = "ActivatbleComponentTest.MyComponent")
	public static final class MyComponent implements Activatable {

		private boolean activated;

		@Inject
		@Named("#name")
		public String name;

		@Override
		public void activate() throws Exception {
			if (activated) {
				throw new RuntimeException("Already activated");
			}
			if (name == null) {
				throw new RuntimeException("Null injectable field when activate() was called");
			}
			this.activated = true;
		}

	}
}
