package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Set;

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

	public static final String CONFIG_DIRECTORY = "src/test/resources/ComponentTest";

	private static final Logger logger = LoggerFactory.getLogger(ComponentTest.class);

	public static final class InjectComponent1 extends InjectorTest {

		@Before
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
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
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
		}

		@Test
		public void test() {
			assertEquals("basic1", get(TestCase.class).getName());
		}

		public static final class TestCase {
			@Inject
			@Named("basic1")
			private BasicComponenInterface basicComponent;

			public String getName() {
				return ((BasicComponent) basicComponent).getName();
			}

		}
	}

	public static final class InjectComponent1BySuperclass extends InjectorTest {

		@Before
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
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
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
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
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
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
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
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
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
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
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
		}

		@Test
		public void test() {
			TestCase testCase = get(TestCase.class);
			assertEquals("external", testCase.external.name);
		}

		public static final class TestCase {

			@Inject
			@Named("external")
			private ExternalComponent external;

		}
	}

	public static final class ReadGlobalConfiguration extends InjectorTest {

		private TestCase testCase;

		@Before
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
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

	public static final class ObjectList extends InjectorTest {

		@Before
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
		}

		@Test
		public void testName() {
			TestCase testCase = get(TestCase.class);
			assertEquals(7, testCase.set.size());
		}

		public static final class TestCase {
			@Inject
			public Set<Object> set;
		}
	}

	public static final class MultibindingContainsSameInstances extends InjectorTest {

		@Before
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
		}

		@Test
		public void test() {
			TestCase testCase = get(TestCase.class);
			assertEquals(3, testCase.list.size());
			assertSame(getFromSet(testCase.list, "basic1"), testCase.comp1);
			assertSame(getFromSet(testCase.list, "basic2"), testCase.comp2);
			assertSame(getFromSet(testCase.list, "basic3"), testCase.comp3);
		}

		private BasicComponent getFromSet(Set<BasicComponent> list, String name) {
			for (BasicComponent component : list) {
				if (component.getName().equals(name)) {
					return component;
				}
			}
			Assert.fail("Could not find " + name);
			return null;
		}

		public static final class TestCase {

			@Inject
			private Set<BasicComponent> list;

			@Inject
			@Named("basic1")
			private BasicComponent comp1;

			@Inject
			@Named("basic2")
			private BasicComponent comp2;

			@Inject
			@Named("basic3")
			private BasicComponent comp3;

		}
	}

	public static final class MultibindingsByInterface extends InjectorTest {

		@Before
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
		}

		@Test
		public void test() {
			TestCase testCase = get(TestCase.class);
			assertEquals(3, testCase.set.size());
			assertSame(getFromSet(testCase.set, "basic1"), testCase.comp1);
			assertSame(getFromSet(testCase.set, "basic2"), testCase.comp2);
			assertSame(getFromSet(testCase.set, "basic3"), testCase.comp3);
		}

		private BasicComponent getFromSet(Set<BasicComponenInterface> list, String name) {
			for (BasicComponenInterface component : list) {
				BasicComponent basicComponent = (BasicComponent) component;
				if (basicComponent.getName().equals(name)) {
					return basicComponent;
				}
			}
			Assert.fail("Could not find " + name);
			return null;
		}

		public static final class TestCase {

			@Inject
			private Set<BasicComponenInterface> set;

			@Inject
			@Named("basic1")
			private BasicComponent comp1;

			@Inject
			@Named("basic2")
			private BasicComponent comp2;

			@Inject
			@Named("basic3")
			private BasicComponent comp3;

		}
	}
	
	public static final class DefaultBindingForSingleNoNameComponentAndInterface extends InjectorTest {
		
		private TestCase testCase;

		@Before
		public void init() throws Exception {
			setup(CONFIG_DIRECTORY);
			this.testCase = get(TestCase.class);
		}

		@Test
		public void test() {
			assertEquals(555, testCase.noNameComponent.someNumber);
			assertEquals(555, testCase.noNameInterface.getSomeNumber());
		}
		
		public static final class TestCase {
			
			@Inject
			public NoNameInterface noNameInterface;
			
			@Inject
			private NoNameComponent noNameComponent;
		}
	}

}

interface ComponentInterface {
	public String getName();
}

interface BasicComponenInterface {

}

abstract class AbstractComponent implements ComponentInterface {

}

@Component("test.basic_component")
final class BasicComponent extends AbstractComponent implements BasicComponenInterface {

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

@Component("test.external_component")
final class ExternalComponent {

	@Inject
	@Named("#name")
	public String name;
}

interface NoNameInterface {
	public int getSomeNumber();
}

@Component("test.no_name_component")
final class NoNameComponent implements NoNameInterface{
	
	@Inject
	@Named("#number")
	public int someNumber;

	@Override
	public int getSomeNumber() {
		return someNumber;
	}
}