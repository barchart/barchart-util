package com.barchart.util.guice;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

public class ActivatableComponentTest extends InjectorTest {

	@Before
	public void setup() throws Exception {
		super.setup("src/test/resources/ActivatableComponentTest");
	}

	@Test
	public void test() {
		final MyComponent myComponent1 = get(MyComponent.class);
		final MyComponent myComponent2 = get(MyComponent.class);
		assertTrue(myComponent1.activated);
		assertTrue(myComponent2.activated);
		assertSame(myComponent1, myComponent2);
	}

	@Test
	public void testAnno() {
		final MyAnnoComponent myComponent1 = get(MyAnnoComponent.class);
		final MyAnnoComponent myComponent2 = get(MyAnnoComponent.class);
		assertTrue(myComponent1.activated);
		assertTrue(myComponent2.activated);
		assertSame(myComponent1, myComponent2);
	}

	@Component("ActivatableComponentTest.MyComponent")
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

	@Component("ActivatableComponentTest.MyAnnoComponent")
	public static final class MyAnnoComponent {

		private boolean activated;

		@Inject
		@Named("#name")
		public String name;

		@Activate
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
