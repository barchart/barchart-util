package com.barchart.util.guice;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;

import com.barchart.util.guice.encryption.Decrypter;
import com.google.inject.AbstractModule;

public class ConfigDecryptionTest {

	private static final String CONFIGURATION_DIRECTORY = "src/test/resources/ConfigDecryptTest";

	@Test
	public void testDefault() throws Exception {
		GuiceConfigBuilder.create()
				.setDirectory(CONFIGURATION_DIRECTORY)
				.build()
				.getInstance(EncryptedConf.class)
				.test();
	}

	@Test
	public void testDecrypt() throws Exception {
		GuiceConfigBuilder.create()
				.setDirectory(CONFIGURATION_DIRECTORY)
				.setDecrypter(new TestDecrypter())
				.build()
				.getInstance(DecryptedConf.class)
				.test();
	}

	@Component("encrypted.component")
	private static final class EncryptedConf extends TestCase {

		@Inject
		@Named("#encrypted")
		private String value;

		@Override
		void test() {
			assertEquals("encrypted", value);
		}

	}

	@Component("decrypted.component")
	private static final class DecryptedConf extends TestCase {

		@Inject
		@Named("decrypted/#encrypted")
		private String value;

		@Override
		void test() {
			assertEquals("decrypted", value);
		}

	}

	public static final class TestDecrypter implements Decrypter {

		@Override
		public byte[] decrypt(final byte[] input) {
			return "decrypted".getBytes();
		}

	}

	public static final class DecryptModule extends AbstractModule {

		@Override
		protected void configure() {
			bind(Decrypter.class).to(TestDecrypter.class);
		}

	}

}
