package com.barchart.util.thread.test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.Callable;

public final class CallableTest {

	private CallableTest() {
	}

	/**
	 * Defaults to wait for ten seconds.
	 */
	public static void waitFor(final Callable<Boolean> condition)
			throws Exception {
		waitFor(condition, null, 1000);
	}

	/**
	 * Defaults to wait for ten seconds.
	 */
	public static void waitFor(final Callable<Boolean> condition,
			final String message) throws Exception {
		waitFor(condition, message, 1000);
	}

	/**
	 * Defaults to wait for ten seconds.
	 */
	public static void waitFor(final Callable<Boolean> condition,
			final long maxTime) throws Exception {
		waitFor(condition, null, maxTime);
	}

	public static void waitFor(final Callable<Boolean> condition,
			String message, final long maxTime) throws Exception {

		if (message == null) {
			message = "waitFor() time expired before assertion passed";
		}

		final long start = System.currentTimeMillis();

		while (!condition.call()) {
			if (System.currentTimeMillis() > start + maxTime) {
				throw new Exception(message);
			}
			Thread.sleep(1);
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