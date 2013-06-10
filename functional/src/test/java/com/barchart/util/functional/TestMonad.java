/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.functional;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class TestMonad {

	// MJS: Here we get a bytestring from a String object. Notice no checks for
	// null or exception handling needed
	private final MonadicGetter<String, byte[]> strToBytes =
			new MonadicGetter<String, byte[]>() {

				@Override
				protected byte[] get(String sessionId) {
					return sessionId.getBytes();
				}
			};

	private class MonadHelper {

		MonadHelper() {
		}

		public boolean result;
	};

	@Test
	public void testMonad() {

		final String str = "some test";
		final MonadHelper state = new MonadHelper();

		MonadicSetter<MonadHelper, byte[]> setResult =
				new MonadicSetter<MonadHelper, byte[]>(state) {

					@Override
					protected void set(MonadHelper state, byte[] bytes) {

						byte[] set = str.getBytes();
						state.result = Arrays.equals(bytes, set);
					}
				};

		try {
			strToBytes.apply(str).bind(setResult);

		} catch (Failure e) {
		}

		assertTrue(state.result);
	}
}
