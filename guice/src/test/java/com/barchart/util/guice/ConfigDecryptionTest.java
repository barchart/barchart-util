package com.barchart.util.guice;

import static org.junit.Assert.*;

import javax.inject.Named;

import org.junit.Test;

import com.barchart.util.guice.encryption.Decrypter;
import com.barchart.util.guice.encryption.Encrypted;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;

public class ConfigDecryptionTest {

	private static final String STATIC_CONFIG = "src/test/resources/StaticDecryptTest";
	private static final String CONFIG_CONFIG = "src/test/resources/ConfigDecryptTest";

	@Test
	public void testNoDecrypter() throws Exception {
		GuiceConfigBuilder.create()
				.setDirectory(STATIC_CONFIG)
				.build()
				.getInstance(NoDecrypter.class)
				.test();
	}

	@Test
	public void testStaticDecrypter() throws Exception {
		GuiceConfigBuilder.create()
				.setDirectory(STATIC_CONFIG)
				.setDecrypter(new TestDecrypter())
				.build()
				.getInstance(StaticDecrypter.class)
				.test();
	}

	@Test
	public void testConfigDecrypter() throws Exception {
		GuiceConfigBuilder.create()
				.setDirectory(CONFIG_CONFIG)
				.build()
				.getInstance(ConfigDecrypter.class)
				.test();
	}

	@Component("nodecrypter")
	private static final class NoDecrypter extends TestCase {

		@Inject
		@Named("#encrypted")
		private String value;

		@Override
		void test() {
			assertEquals("encrypted", value);
		}

	}

	@Component("staticdecrypter")
	private static final class StaticDecrypter extends TestCase {

		@Inject(optional = true)
		@Encrypted("#encrypted")
		private String value;

		@Inject(optional = true)
		@Encrypted("#encrypted")
		private byte[] raw;

		@Override
		void test() {
			assertEquals("decrypted", value);
			assertArrayEquals("decrypted".getBytes(), raw);
		}

	}

	@Component("configdecrypter")
	private static final class ConfigDecrypter extends TestCase {

		@Inject(optional = true)
		@Encrypted("#encrypted")
		private String value;

		@Inject(optional = true)
		@Encrypted("#encrypted")
		private byte[] raw;

		@Override
		void test() {
			assertEquals("config", value);
			assertArrayEquals("config".getBytes(), raw);
		}

	}

	public static final class TestDecrypter implements Decrypter {

		@Override
		public byte[] decrypt(final byte[] input) {
			return "decrypted".getBytes();
		}

	}

	public static final class TestConfigDecrypter implements Decrypter {

		@Inject
		@Named("#output")
		private String output = "decrypted";

		@Override
		public byte[] decrypt(final byte[] input) {
			return output.getBytes();
		}

	}

	public static final class DecryptModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(Decrypter.class).to(TestDecrypter.class);
		}

	}

}
