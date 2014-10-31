package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
//import javax.inject.Named;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

@RunWith(Enclosed.class)
public class ComponentTest {

	private static final Logger logger = LoggerFactory.getLogger(ComponentTest.class);

	public static final class InjectComponent1 extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			assertEquals("basic1", get(TestCase.class).basicComponent.getName());
		}

		public static final class TestCase {
			@Inject
			@Named("basic1")
			private BasicComponent basicComponent;

		}
	}

	public static final class InjectComponent1ByInterface extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			assertEquals("basic1", get(TestCase.class).getName());
		}

		public static final class TestCase {
			@Inject
			@Named("basic1")
			private BasicComponentnterface basicComponent;

			public String getName() {
				return ((BasicComponent) basicComponent).getName();
			}

		}
	}

	public static final class InjectComponent1BySuperclass extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			assertEquals("basic1", get(TestCase.class).basicComponent.getName());
		}

		public static final class TestCase {

			@Inject
			@Named("basic1")
			private AbstractComponent basicComponent;

		}

	}

	public static final class InjectComponent1BySuperclassesInterface extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			assertEquals("basic1", get(TestCase.class).basicComponent.getName());
		}

		public static final class TestCase {
			@Inject
			@Named("basic1")
			private ComponentInterface basicComponent;

		}
	}

	public static final class InjectComponent1BySuperSuperClass extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			assertEquals("basic1", get(TestCase.class).getName());
		}

		public static final class TestCase {
			@Inject
			@Named("basic1")
			private Object basicComponent;

			public Object getName() {
				return ((BasicComponent) basicComponent).getName();
			}
		}
	}

	public static final class DuplicateComponentInjections extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			TestCase testCase = get(TestCase.class);
			Assert.assertSame(testCase.basicComponentField1, testCase.basicComponentField2);
		}

		public static final class TestCase {
			@Inject
			@Named("basic1")
			private BasicComponent basicComponentField1;

			@Inject
			@Named("basic1")
			private BasicComponent basicComponentField2;

		}
	}

	public static final class Component2ReferencesComponent1 extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			TestCase testCase = get(TestCase.class);
			assertEquals("compound1", testCase.compoundComponent.getName());
			assertEquals("basic1", testCase.compoundComponent.basicComponent.getName());
		}

		public static final class TestCase {

			@Inject
			@Named("compound1")
			private CompoundComponent compoundComponent;

		}
	}

	public static final class ExternalComponentFile extends InjectorTest {

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
		}

		@Test
		public void test() {
			TestCase testCase = get(TestCase.class);
			assertEquals("external", testCase.external.getName());
		}

		public static final class TestCase {

			@Inject
			@Named("external")
			private BasicComponent external;

		}
	}

	public static final class ReadGlobalConfiguration extends InjectorTest {

		private TestCase testCase;

		@Before
		public void init() {
			setup("src/test/resources/componenttest");
			this.testCase = get(TestCase.class);
		}

		@Test
		public void testName() {
			assertEquals("configreader", testCase.reader.name);
		}
		
		@Test
		public void testCommonString() {
			assertEquals("common", testCase.reader.commonString);
		}
		
		@Test
		public void testCommonObject() {
			assertEquals("common_object", testCase.reader.commonConfigObject.getString("value"));
		}
		
		@Test
		public void testNumberFromOtherFile() {
			assertEquals(42, testCase.reader.numberFromOtherFile);
		}
		
		@Test
		public void testComponentConfig() {
			assertEquals("configreader", testCase.reader.componentConfig.getString("name"));
		}

		public static final class TestCase {

			@Inject
			@Named("configreader")
			private CommonConfigReader reader;

		}

	}
}

interface ComponentInterface {
	public String getName();
}

interface BasicComponentnterface {

}

abstract class AbstractComponent implements ComponentInterface {

}

@Component("test.basic_component")
final class BasicComponent extends AbstractComponent implements BasicComponentnterface {

	@Inject
	@Named("#name")
	public String name;

	@Override
	public String getName() {
		return name;
	}

}

@Component("test.compound_component")
final class CompoundComponent extends AbstractComponent {

	@Inject
	@Named("#name")
	public String name;

	@Inject
	@Named("basic1")
	public BasicComponent basicComponent;

	@Override
	public String getName() {
		return name;
	}

}

@Component("test.common_config_reader")
final class CommonConfigReader {

	@Inject
	@Named("#name")
	public String name;

	@Inject
	@Named("#")
	public Config componentConfig;
	
	@Inject
	@Named("common_config_string")
	public String commonString;

	@Inject
	@Named("common_config_object")
	public Config commonConfigObject;

	@Inject
	@Named("other/number")
	public int numberFromOtherFile;
}
