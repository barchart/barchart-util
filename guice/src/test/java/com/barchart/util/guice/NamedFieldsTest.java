package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.typesafe.config.Config;

public class NamedFieldsTest {

	private static final double DOUBLE_DELTA = 0.0;

	private static final float FLOAT_DELTA = 0.0f;

	private static final String STRING_FIELD = "stringField";

	private static final String CONFIG_FIELD = "configField";

	private static final String SMALL_INTEGER_NUMBER_FIELD = "smallIntegerNumberField";

	private static final String BOOLEAN_FIELD = "booleanField";

	private static final String STRING_LIST_FIELD = "stringListField";

	private static final String NUMBER_LIST_FIELD = "numberListField";

	public static final String BOOLEAN_LIST_FIELD = "booleanListField";

	public static final String CONFIG_LIST_FIELD = "configListField";

	public static final String EMPTY_LIST = "emptyList";

	private static final class StringField extends FieldTarget {

		@Inject
		@Named(STRING_FIELD)
		private String field;

		@Override
		void test() {
			assertEquals("hello", field);
		}
	}

	private static final class ConfigField extends FieldTarget {

		@Inject
		@Named(CONFIG_FIELD)
		private Config field;

		@Override
		void test() {
			assertEquals("conf1", field.getString("id"));
		}

	}

	private static final class SmallIntegerNumberToIntegerField extends FieldTarget {

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private int primitiveField;

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private Integer objectField;

		@Override
		void test() {
			assertEquals(42, primitiveField);
			assertEquals(Integer.valueOf(42), objectField);
		}
	}

	private static final class SmallIntegerNumberToLongField extends FieldTarget {

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private long primitiveField;

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private Long objectField;

		@Override
		void test() {
			assertEquals(42L, primitiveField);
			assertEquals(Long.valueOf(42L), objectField);
		}
	}

	private static final class SmallIntegerNumberToShortField extends FieldTarget {
		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private short primitiveField;

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private Short objectField;

		@Override
		void test() {
			assertEquals((short) 42, primitiveField);
			assertEquals(Short.valueOf((short) 42), objectField);
		}
	}

	private static final class SmallIntegerNumberToByteField extends FieldTarget {
		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private byte primitiveField;

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private Byte objectField;

		@Override
		void test() {
			assertEquals((byte) 42, primitiveField);
			assertEquals(Byte.valueOf((byte) 42), objectField);
		}
	}

	private static final class SmallIntegerNumberToDoubleField extends FieldTarget {

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private double primitiveField;

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private Double objectField;

		@Override
		void test() {
			assertEquals(42.0, primitiveField, DOUBLE_DELTA);
			assertEquals(Double.valueOf(42.0), objectField);
		}
	}

	private static final class SmallIntegerNumberToFloatField extends FieldTarget {

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private float primitiveField;

		@Inject
		@Named(SMALL_INTEGER_NUMBER_FIELD)
		private Float objectField;

		@Override
		void test() {
			assertEquals(42.0f, primitiveField, FLOAT_DELTA);
			assertEquals(Float.valueOf(42.0f), objectField);
		}

	}

	private static final class BooleanField extends FieldTarget {

		@Inject
		@Named(BOOLEAN_FIELD)
		private boolean primitiveField;

		@Inject
		@Named(BOOLEAN_FIELD)
		private Boolean objectField;

		@Override
		void test() {
			assertEquals(true, primitiveField);
			assertEquals(Boolean.TRUE, objectField);
		}

	}

	private static final class StringListField extends FieldTarget {

		@Inject
		@Named(STRING_LIST_FIELD)
		private List<String> field;

		@Override
		void test() {
			assertEquals(Arrays.asList("a", "b", "c"), field);
		}

	}

	private static final class LongListField extends FieldTarget {

		@Inject
		@Named(NUMBER_LIST_FIELD)
		private List<Long> field;

		@Override
		void test() {
			assertEquals(Arrays.asList(1L, 2L, 3L), field);
		}

	}

	private static final class IntListField extends FieldTarget {

		@Inject
		@Named(NUMBER_LIST_FIELD)
		private List<Integer> field;

		@Override
		void test() {
			assertEquals(Arrays.asList(1, 2, 3), field);
		}

	}

	private static final class ShortListField extends FieldTarget {

		@Inject
		@Named(NUMBER_LIST_FIELD)
		private List<Short> field;

		@Override
		void test() {
			assertEquals(Arrays.asList((short) 1, (short) 2, (short) 3), field);
		}

	}

	private static final class ByteListField extends FieldTarget {

		@Inject
		@Named(NUMBER_LIST_FIELD)
		private List<Byte> field;

		@Override
		void test() {
			assertEquals(Arrays.asList((byte) 1, (byte) 2, (byte) 3), field);
		}

	}

	private static final class DoubleListField extends FieldTarget {

		@Inject
		@Named(NUMBER_LIST_FIELD)
		private List<Double> field;

		@Override
		void test() {
			assertEquals(Arrays.asList(1d, 2d, 3d), field);
		}

	}

	private static final class FloatListField extends FieldTarget {

		@Inject
		@Named(NUMBER_LIST_FIELD)
		private List<Float> field;

		@Override
		void test() {
			assertEquals(Arrays.asList(1f, 2f, 3f), field);
		}

	}

	private static final class ConfigListField extends FieldTarget {

		@Inject
		@Named(CONFIG_LIST_FIELD)
		private List<Config> field;

		@Override
		void test() {
			assertEquals(2, field.size());
			assertEquals("conf1", field.get(0).getString("id"));
			assertEquals("conf2", field.get(1).getString("id"));
		}

	}

	private static final class BooleanListField extends FieldTarget {

		@Inject
		@Named(BOOLEAN_LIST_FIELD)
		private List<Boolean> field;

		@Override
		void test() {
			assertEquals(Arrays.asList(false, true, true, false), field);
		}
	}

	private static final class EmptyLists extends FieldTarget {

		@Inject
		@Named(EMPTY_LIST)
		private List<String> stringListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Boolean> booleanListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Long> longListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Integer> integerListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Short> shortListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Byte> byteListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Double> doubleListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Float> floatListField;

		@Inject
		@Named(EMPTY_LIST)
		private List<Config> configListField;

		@Override
		void test() {
			assertTrue(stringListField.isEmpty());
			assertTrue(booleanListField.isEmpty());
			assertTrue(longListField.isEmpty());
			assertTrue(integerListField.isEmpty());
			assertTrue(shortListField.isEmpty());
			assertTrue(byteListField.isEmpty());
			assertTrue(doubleListField.isEmpty());
			assertTrue(floatListField.isEmpty());
			assertTrue(configListField.isEmpty());
		}

	}

	private static final class TypeMismatchList extends FieldTarget {

		// String list in the config getting injected into a List<Integer> ?!?
		// Unchecked generic casting makes it possible. Should be caught on
		// initialization,
		// and not when we try to read from the list
		@Inject
		@Named(STRING_LIST_FIELD)
		private List<Integer> field;

		@Override
		void test() {
			fail("This test case should fail during injection because of a badly typed generic list.");
		}

	}

	@Test
	public void testStringField() {
		runTest(StringField.class);
	}

	@Test
	public void testConfigField() {
		runTest(ConfigField.class);
	}

	@Test
	public void testSmallIntegerNumberToLongField() {
		runTest(SmallIntegerNumberToLongField.class);
	}

	@Test
	public void testSmallIntegerNumberToIntegerField() {
		runTest(SmallIntegerNumberToIntegerField.class);
	}

	@Test
	public void testSmallIntegerNumberToShortField() {
		runTest(SmallIntegerNumberToShortField.class);
	}

	@Test
	public void testSmallIntegerNumberToByteField() {
		runTest(SmallIntegerNumberToByteField.class);
	}

	@Test
	public void testSmallIntegerNumberToDoubleField() {
		runTest(SmallIntegerNumberToDoubleField.class);
	}

	@Test
	public void testSmallIntegerNumberToFloatField() {
		runTest(SmallIntegerNumberToFloatField.class);
	}

	@Test
	public void testBooleanField() {
		runTest(BooleanField.class);
	}

	@Test
	public void testStringListField() {
		runTest(StringListField.class);
	}

	@Test
	public void testLongListField() {
		runTest(LongListField.class);
	}

	@Test
	public void testIntListField() {
		runTest(IntListField.class);
	}

	@Test
	public void testShortListField() {
		runTest(ShortListField.class);
	}

	@Test
	public void testByteListField() {
		runTest(ByteListField.class);
	}

	@Test
	public void testDoubleListField() {
		runTest(DoubleListField.class);
	}

	@Test
	public void testFloatListField() {
		runTest(FloatListField.class);
	}

	@Test
	public void testBooleanListField() {
		runTest(BooleanListField.class);
	}

	@Test
	public void testConfigListField() {
		runTest(ConfigListField.class);
	}

	@Test
	public void testEmptyLists() {
		runTest(EmptyLists.class);
	}

	@Test(expected = ConfigurationException.class)
	public void testTypeMismatchList() {
		runTest(TypeMismatchList.class);
	}

	private Injector injector;

	@Before
	public void setup() {
		ConfigModule configModule = new ConfigModule(new File("src/test/resources/namedfieldtest"));
		this.injector = Guice.createInjector(configModule);
	}

	private <T> T get(Class<T> clazz) {
		return injector.getInstance(clazz);
	}

	private void runTest(Class<? extends FieldTarget> clazz) {
		get(clazz).test();
	}

	private static abstract class FieldTarget {
		@Test
		abstract void test();
	}
}
