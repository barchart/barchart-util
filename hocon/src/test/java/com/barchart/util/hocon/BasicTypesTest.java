package com.barchart.util.hocon;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class BasicTypesTest {

	private static final Logger logger = LoggerFactory.getLogger(BasicTypesTest.class);

	public static final String CONFIG_FILE = "src/test/resources/basictypes.conf";

	private static final double DELTA = Double.MIN_VALUE;

	public static enum TestEnum {
		OPTION_A,
		OPTION_B, ;
	}

	public static interface TestConfigInterface {

		public String getStringValue();

		public byte getByteValue();

		public short getShortValue();

		public int getIntValue();

		public long getLongValue();

		public float getFloatValue();

		public double getDoubleValue();

		public boolean getBooleanValue();

		public byte getByteObjectValue();

		public short getShortObjectValue();

		public int getIntObjectValue();

		public long getLongObjectValue();

		public float getFloatObjectValue();

		public double getDoubleObjectValue();

		public boolean getBooleanObjectValue();

		public List<String> getStringList();

		public List<Integer> getIntegerList();

		public File getTestFile();

		public Config getTypesafeConfig();

		public TestEnum getTestEnum();

	}

	private static Config rawConfig;

	private static TestConfigInterface proxyConfig;

	@BeforeClass
	public static void setup() {
		HoconProxyLoader loader = new HoconProxyLoader();
		rawConfig = ConfigFactory.parseFile(new File(CONFIG_FILE));
		proxyConfig = loader.loadProxy(TestConfigInterface.class, new File(CONFIG_FILE));
	}

	@Test
	public void testStringValue() {
		assertEquals(rawConfig.getString("string_value"), proxyConfig.getStringValue());
	}

	@Test
	public void testByteValue() {
		assertEquals((byte) rawConfig.getInt("byte_value"), proxyConfig.getByteValue());
	}

	@Test
	public void testShortValue() {
		assertEquals((short) rawConfig.getInt("short_value"), proxyConfig.getShortValue());
	}

	@Test
	public void testIntegerValue() {
		assertEquals(rawConfig.getInt("int_value"), proxyConfig.getIntValue());
	}

	@Test
	public void testLongValue() {
		assertEquals(rawConfig.getLong("long_value"), proxyConfig.getLongValue());
	}

	@Test
	public void testFloatValue() {
		assertEquals((float) rawConfig.getDouble("float_value"), proxyConfig.getFloatValue(), DELTA);
	}

	@Test
	public void testDoubleValue() {
		assertEquals(rawConfig.getDouble("double_value"), proxyConfig.getDoubleValue(), DELTA);
	}

	@Test
	public void testBooleanValue() {
		assertEquals(rawConfig.getBoolean("boolean_value"), proxyConfig.getBooleanValue());
	}

	@Test
	public void testByteObjectValue() {
		assertEquals((byte) rawConfig.getInt("byte_object_value"), proxyConfig.getByteObjectValue());
	}

	@Test
	public void testShortObjectValue() {
		assertEquals((short) rawConfig.getInt("short_object_value"), proxyConfig.getShortObjectValue());
	}

	@Test
	public void testIntegerObjectValue() {
		assertEquals(rawConfig.getInt("int_object_value"), proxyConfig.getIntObjectValue());
	}

	@Test
	public void testLongObjectValue() {
		assertEquals(rawConfig.getLong("long_object_value"), proxyConfig.getLongObjectValue());
	}

	@Test
	public void testFloatObjectValue() {
		assertEquals((float) rawConfig.getDouble("float_object_value"), proxyConfig.getFloatObjectValue(), DELTA);
	}

	@Test
	public void testDoubleObjectValue() {
		assertEquals(rawConfig.getDouble("double_object_value"), proxyConfig.getDoubleObjectValue(), DELTA);
	}

	@Test
	public void testBooleanObjectValue() {
		assertEquals(rawConfig.getBoolean("boolean_object_value"), proxyConfig.getBooleanObjectValue());
	}

	@Test
	public void testStringList() {
		assertEquals(rawConfig.getStringList("string_list"), proxyConfig.getStringList());
	}

	@Test
	public void testIntegerList() {
		assertEquals(rawConfig.getIntList("integer_list"), proxyConfig.getIntegerList());
	}

	@Test
	public void testFileValue() {
		assertEquals(new File(rawConfig.getString("test_file")), proxyConfig.getTestFile());
	}

	@Test
	public void testTypesafeConfig() {
		assertEquals(rawConfig.getConfig("typesafe_config"), proxyConfig.getTypesafeConfig());
	}

	@Test
	public void testEnum() {
		assertEquals(TestEnum.valueOf(rawConfig.getString("test_enum").toUpperCase()), proxyConfig.getTestEnum());
	}

}
