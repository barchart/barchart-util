package com.barchart.util.common.crypto;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Arrays;

import javax.crypto.Cipher;

import org.junit.Test;

public class TestKerberosUtilities {

	@Test
	public void base64() throws Exception {

		final byte[] source = "base 64 test".getBytes(Charset.forName("UTF-8"));

		final String target = "YmFzZSA2NCB0ZXN0";

		final byte[] bytes = KerberosUtilities.base64decode(target);
		System.err.println("bytes = " + new String(bytes));

		final String chars = KerberosUtilities.base64encode(source);
		System.err.println("chars = " + chars);

		assertTrue(Arrays.equals(source, bytes));
		assertEquals(target, chars);

	}

	@Test
	public void aes() throws Exception {

		final int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
		System.err.println("maxKeyLen = " + maxKeyLen);

		for (int k = 0; k < 3; k++) {

			final byte[] secretKey = KerberosUtilities.defaultSecretKey(
					"account", "secret");
			System.err.println("secretKey = " + secretKey.length + " / "
					+ new String(secretKey));

			final byte[] ciferText = KerberosUtilities.defaultEncrypt(
					"test".getBytes(), secretKey);
			System.err.println("ciferText = " + ciferText.length + " / "
					+ new String(ciferText));

			final byte[] plainText = KerberosUtilities.defaultDecrypt(
					ciferText, secretKey);
			System.err.println("plainText = " + new String(plainText));

			assertEquals("test", new String(plainText));

		}

	}

	@Test
	public void signature() {

		final byte[] message = "message".getBytes();

		final byte[] secretKey = "secret".getBytes();

		final byte[] signature = KerberosUtilities.defaultSignatureCreate(
				message, secretKey);

		System.err.println("signature = " + signature.length);

		final String source = KerberosUtilities.bytesToHex(signature);

		System.err.println("source = " + source);

		final String target = "8b5f48702995c1598c573db1e21866a9b825d4a794d169d7060a03605796360b";

		System.err.println("target = " + target);

		assertEquals(source, target);

	}

}
