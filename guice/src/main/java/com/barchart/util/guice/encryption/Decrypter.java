package com.barchart.util.guice.encryption;

/**
 * Value decryption service.
 */
public interface Decrypter {

	byte[] decrypt(byte[] input);

}
