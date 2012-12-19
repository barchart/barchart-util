/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestFutureNotifier {

	private Executor executor;

	@Before
	public void setUp() {
		executor = Executors.newFixedThreadPool(4);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testImmediate() throws Exception {

		final FutureNotifier<String> notifier = new FutureNotifier<String>();
		notifier.succeed("RESULT");

		final String result = notifier.get();
		assertTrue(notifier.isDone());
		assertFalse(notifier.isCancelled());
		assertEquals("RESULT", result);

	}

	@Test
	public void testDelayed() throws Exception {

		final FutureNotifier<String> notifier = new FutureNotifier<String>();
		executor.execute(new DelayedCallback(notifier, "RESULT", 100, null));

		final String result = notifier.get();
		assertTrue(notifier.isDone());
		assertFalse(notifier.isCancelled());
		assertEquals("RESULT", result);

	}

	@Test
	public void testCallback() throws Exception {

		final AtomicInteger count = new AtomicInteger(0);
		final FutureNotifier<String> notifier = new FutureNotifier<String>();

		notifier.addResultListener(new FutureListener<String>() {
			@Override
			public void resultAvailable(final Future<String> result)
					throws Exception {
				count.incrementAndGet();
			}
		});

		notifier.succeed("RESULT");

		assertEquals("RESULT", notifier.get());
		assertEquals(1, count.get());

	}

	@Test(expected = ExecutionException.class)
	public void testFailure() throws Exception {

		final FutureNotifier<String> notifier = new FutureNotifier<String>();
		notifier.fail(new Exception("failed"));
		notifier.get();

	}

	@Test(expected = IllegalStateException.class)
	public void testDuplicateSucceed() throws Exception {

		final FutureNotifier<String> notifier = new FutureNotifier<String>();
		notifier.succeed("RESULT");
		notifier.succeed("RESULT2");

		assertEquals("RESULT", notifier.get());

	}

	@Test(expected = IllegalStateException.class)
	public void testSucceedFailure() throws Exception {

		final FutureNotifier<String> notifier = new FutureNotifier<String>();
		notifier.succeed("RESULT");
		notifier.fail(new Exception());

	}

	@Test(expected = CancellationException.class)
	public void testCancel() throws Exception {

		final FutureNotifier<String> notifier = new FutureNotifier<String>();
		executor.execute(new DelayedCallback(notifier, "RESULT", 1000, null));
		notifier.cancel(true);

		notifier.get();

	}

	// @Test
	// Last run: around 1 million / sec on one CPU
	public void testPerformance() throws Exception {

		final long start = System.currentTimeMillis();
		final AtomicInteger count = new AtomicInteger(0);

		for (int i = 0; i < 100000; i++) {
			final FutureNotifier<String> notifier =
					new FutureNotifier<String>();
			notifier.addResultListener(new FutureListener<String>() {
				@Override
				public void resultAvailable(final Future<String> result)
						throws Exception {
					count.incrementAndGet();
				}
			});
			notifier.succeed("RESULT");
		}

		final long elapsed = System.currentTimeMillis() - start;

		assertEquals(100000, count.get());

		System.out.println(elapsed);

	}

	private class DelayedCallback implements Runnable {

		private final FutureNotifier<String> callback;
		private final String result;
		private final long delay;
		private final Throwable error;

		DelayedCallback(final FutureNotifier<String> callback_,
				final String result_, final long delay_, final Throwable error_) {
			callback = callback_;
			result = result_;
			delay = delay_;
			error = error_;
		}

		@Override
		public void run() {

			try {
				Thread.sleep(delay);
			} catch (final InterruptedException e) {
				callback.fail(e);
			}

			if (error == null) {
				callback.succeed(result);
			} else {
				callback.fail(error);
			}

		}

	}

}
