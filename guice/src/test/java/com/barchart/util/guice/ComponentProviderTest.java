package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.junit.Before;
import org.junit.Test;

public class ComponentProviderTest extends InjectorTest {

	@Before
	public void setup() throws Exception {
		super.setup("src/test/resources/ComponentProviderTest");
	}

	@Test
	public void test() {
		ComplexComponent complexComponent = get(ComplexComponent.class);
		assertEquals("val1", complexComponent.param1);
		assertEquals("val2", complexComponent.param2);
		assertEquals("val3", complexComponent.param3);
	}

	private static final class ComplexComponent {

		public final String param1;

		public final String param2;

		public final String param3;

		public ComplexComponent(String param1, String param2, String param3) {
			this.param1 = param1;
			this.param2 = param2;
			this.param3 = param3;
		}

	}

	@Component("ComponentProviderTest.ComplexComponent")
	private static final class ComplexComponentProvider implements Provider<ComplexComponent> {

		@Inject
		@Named("#param1")
		private String param1;

		@Inject
		@Named("#param2")
		private String param2;

		@Inject
		@Named("#param3")
		private String param3;

		public ComplexComponent get() {
			return new ComplexComponent(param1, param2, param3);
		}

	}

}
