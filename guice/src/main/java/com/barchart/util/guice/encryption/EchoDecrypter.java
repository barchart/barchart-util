package com.barchart.util.guice.encryption;

/**
 * Echo decrypter that spits back the same value it was given. Useful for test environments.
 */
public class EchoDecrypter implements Decrypter {

	@Override
	public byte[] decrypt(final byte[] input) {
		return input;
	}

}