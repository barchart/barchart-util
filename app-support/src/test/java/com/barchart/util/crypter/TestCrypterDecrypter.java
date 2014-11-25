package com.barchart.util.crypter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.barchart.util.guice.encryption.Decrypter;

public class TestCrypterDecrypter {

	// "pip install crypter" first, and run daemon. Manual test using jeremy's private key.
	// @Test
	public void testDecrypt() throws Exception {

		final Decrypter d = new CrypterDecrypter("/usr/local/bin/crypter-client");

		final String encrypted = "-----BEGIN PKCS7-----\n"
		+ "MIIBeQYJKoZIhvcNAQcDoIIBajCCAWYCAQAxggEhMIIBHQIBADAFMAACAQEwDQYJ\n"
		+ "KoZIhvcNAQEBBQAEggEAHEPw2LFrdqIPYPHnJPP89i1EsTf25u2syhZY2hbTpZYa\n"
		+ "Fty2aLrXF/GoC3bTu+kzP9G8CCYLlb7e1sBKoC6Vw+hlr4Q8A3/nfqRJhY4cziDb\n"
		+ "2R5OobtBxP3X4nDkW4C5dQIC5NrW5EMqSax+FNd+VbahtjALfDSjSRcC6NmxzxR5\n"
		+ "szyimkXjGKbYrFW0nQm7u08K7e/i9/sWzb8+Qcgtm5MgMW9RjQZIMTrjzEcsVHp3\n"
		+ "tqu2xMpK63nRPGSwjAcnrtxk3i2hqDe6y99SQ9F72GlcW4/wUo+H1okEgZdIu1DV\n"
		+ "vzmjkMqvNvcc9zP2CbdUjMibfh1i6PZleWGS7xwiSTA8BgkqhkiG9w0BBwEwHQYJ\n"
		+ "YIZIAWUDBAEqBBBuHRLo1V2WBOVAxUVOgoa2gBAWf2ahbQjNdH+8qxVrejoE\n"
		+ "-----END PKCS7-----\n";

		final byte[] decrypted = d.decrypt(encrypted.getBytes());

		assertEquals("test", new String(decrypted));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalid() throws Exception {
		new CrypterDecrypter("/invalid/path");
	}

}