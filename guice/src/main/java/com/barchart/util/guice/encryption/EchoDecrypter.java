package com.barchart.util.guice.encryption;


public class EchoDecrypter implements Decrypter {

	@Override
	public byte[] decrypt(final byte[] input) {
		return input;
	}

}