/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Executes tasks in a thread pool. submit() method return a FutureCallback
 * instance, to allow easily attaching notification callbacks to jobs.
 * </p>
 * 
 * @author jeremy
 */
public class FutureCallbackExecutor extends ThreadPoolExecutor {

	public FutureCallbackExecutor(final int poolSize, final int maxPoolSize,
			final long keepAlive, final TimeUnit units,
			final BlockingQueue<Runnable> queue) {
		super(poolSize, maxPoolSize, keepAlive, units, queue);
	}

	@Override
	protected <T> FutureCallbackTask<T> newTaskFor(final Callable<T> callable) {
		return new FutureCallbackTask<T>(callable);
	}

	@Override
	protected <T> FutureCallbackTask<T> newTaskFor(final Runnable runnable,
			final T value) {
		return new FutureCallbackTask<T>(runnable, value);
	}

	@Override
	public <T> FutureCallbackTask<T> submit(final Callable<T> callable) {
		final FutureCallbackTask<T> f = newTaskFor(callable);
		execute(f);
		return f;
	}

	@Override
	public <T> FutureCallbackTask<T> submit(final Runnable runnable,
			final T value) {
		final FutureCallbackTask<T> f = newTaskFor(runnable, value);
		execute(f);
		return f;
	}

	@Override
	public FutureCallbackTask<?> submit(final Runnable runnable) {
		final FutureCallbackTask<?> f = newTaskFor(runnable, null);
		execute(f);
		return f;
	}

}
