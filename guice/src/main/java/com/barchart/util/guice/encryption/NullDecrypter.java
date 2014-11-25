package com.barchart.util.guice.encryption;

/**
 * Default decrypter if none is specified. Results in no decrypted values being bound, which throws Guice wiring errors
 * if they are referenced.
 */
public class NullDecrypter implements Decrypter {

	@Override
	public byte[] decrypt(final byte[] input) {
		return null;
	}

}