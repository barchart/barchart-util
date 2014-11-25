package com.barchart.util.crypter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.encryption.Decrypter;
import com.google.common.io.ByteStreams;

public class CrypterDecrypter implements Decrypter {

	private static final Logger logger = LoggerFactory.getLogger(CrypterDecrypter.class);

	private static final String PREFIX = "-----BEGIN PKCS7-----";
	private static final String SUFFIX = "-----END PKCS7-----";

	private final String script;

	public CrypterDecrypter(final String script_) {

		if (!new File(script_).exists()) {
			throw new IllegalArgumentException("Script not found: " + script_);
		}

		script = script_;

	}

	@Override
	public byte[] decrypt(final byte[] input) {

		if (input != null && input.length > 0) {

			try {

				final String encrypted = new String(input, "UTF-8").trim();

				// Check for supported format
				if (encrypted.startsWith(PREFIX) && encrypted.endsWith(SUFFIX)) {

					try {

						// Hand off to local script client
						final Process p = Runtime.getRuntime().exec(new String[] {
								script, "decrypt", encrypted
						});

						p.waitFor();

						if (p.exitValue() != 0) {
							throw new IOException("Crypter exited with code: " + p.exitValue());
						}

						final byte[] decrypted = ByteStreams.toByteArray(p.getInputStream());
						return Arrays.copyOf(decrypted, decrypted.length - 1);

					} catch (final IOException e) {
						logger.warn("Error decrypting value", e);
						throw new RuntimeException(e);
					} catch (final InterruptedException e) {
						Thread.currentThread().interrupt();
						throw new RuntimeException(e);
					}

				}

			} catch (final UnsupportedEncodingException e) {
				// Ignore
			}

		}

		return null;

	}

}
