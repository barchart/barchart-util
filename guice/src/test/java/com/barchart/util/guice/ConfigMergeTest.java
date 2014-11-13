package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

public class ConfigMergeTest extends InjectorTest {

	private static final String CONFIGURATION_DIRECTORY = "src/test/resources/ConfigMergeTest";

	@Test
	public void testMergeConf() {
		get(MergeConf.class).test();
	}

	@Before
	public void setup() throws Exception {
		super.setup(CONFIGURATION_DIRECTORY);
	}

	@Component("test.component")
	private static final class MergeConf extends TestCase {

		@Inject
		@Named("#first")
		private String first;

		@Inject
		@Named("#second")
		private String second;

		@Inject
		@Named("#third")
		private String third;

		@Override
		void test() {
			assertEquals("component", first);
			assertEquals("application", second);
			assertEquals("other", third);
		}

	}

}
