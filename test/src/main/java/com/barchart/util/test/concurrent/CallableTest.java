/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.test.concurrent;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public final class CallableTest {

	private CallableTest() {
	}

	/**
	 * Defaults to wait for ten seconds.
	 */
	public static void waitFor(final Callable<Boolean> condition)
			throws Exception {
		waitFor(condition, null, 10 * 1000);
	}

	/**
	 * Defaults to wait for ten seconds.
	 */
	public static void waitFor(final Callable<Boolean> condition,
			final String message) throws Exception {
		waitFor(condition, message, 10 * 1000);
	}

	/**
	 * Defaults to wait for ten seconds.
	 */
	public static void waitFor(final Callable<Boolean> condition,
			final long maxTime) throws Exception {
		waitFor(condition, null, maxTime);
	}

	public static void waitFor(final Callable<Boolean> condition,
			final String message, final long maxTime) throws Exception {
		waitFor(condition, message, maxTime, 1);
	}

	public static void waitFor(final Callable<Boolean> condition,
			String message, final long maxTime, final long pollInterval)
			throws Exception {

		if (message == null) {
			message = "waitFor() time expired before assertion passed";
		}

		final long start = System.currentTimeMillis();

		while (!condition.call()) {
			if (System.currentTimeMillis() > start + maxTime) {
				throw new TimeoutException(message);
			}
			Thread.sleep(pollInterval);
		}

	}

	public static class CollectionSize implements Callable<Boolean> {

		private final Collection<?> collection;
		private final int expectedSize;

		public CollectionSize(final Collection<?> collection_,
				final int expectedSize_) {
			collection = collection_;
			expectedSize = expectedSize_;
		}

		@Override
		public Boolean call() throws Exception {
			return collection.size() == expectedSize;
		}

	}

	// MJS: Added this as sometimes unit test would block indefinitely at a
	// queue with 2 elements waiting for it to be 1
	public static class CollectionSizeEqualOrAbove implements Callable<Boolean> {

		private final Collection<?> collection;
		private final int expectedSize;

		public CollectionSizeEqualOrAbove(final Collection<?> collection_,
				final int expectedSize_) {
			collection = collection_;
			expectedSize = expectedSize_;
		}

		@Override
		public Boolean call() throws Exception {
			return collection.size() >= expectedSize;
		}

	}

	public static class FieldValue implements Callable<Boolean> {

		private final Object obj;
		private final Object value;

		private final Field field;

		public FieldValue(final Object obj_, final String fieldName_,
				final Object value_) throws NoSuchFieldException,
				SecurityException {

			obj = obj_;
			value = value_;

			field = obj.getClass().getField(fieldName_);

		}

		@Override
		public Boolean call() throws Exception {
			if (value != null) {
				return value.equals(field.get(obj));
			}
			return value == field.get(obj);
		}

	}

}