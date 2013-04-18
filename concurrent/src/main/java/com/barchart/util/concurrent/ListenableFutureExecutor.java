/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
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
public class ListenableFutureExecutor extends ThreadPoolExecutor {

	public ListenableFutureExecutor(final int poolSize, final int maxPoolSize,
			final long keepAlive, final TimeUnit units,
			final BlockingQueue<Runnable> queue) {
		super(poolSize, maxPoolSize, keepAlive, units, queue);
	}

	@Override
	protected <T> ListenableFutureTask<T> newTaskFor(final Callable<T> callable) {
		return new ListenableFutureTask<T>(callable);
	}

	@Override
	protected <T> ListenableFutureTask<T> newTaskFor(final Runnable runnable,
			final T value) {
		return new ListenableFutureTask<T>(runnable, value);
	}

	@Override
	public <T> ListenableFutureTask<T> submit(final Callable<T> callable) {
		final ListenableFutureTask<T> f = newTaskFor(callable);
		execute(f);
		return f;
	}

	@Override
	public <T> ListenableFutureTask<T> submit(final Runnable runnable,
			final T value) {
		final ListenableFutureTask<T> f = newTaskFor(runnable, value);
		execute(f);
		return f;
	}

	@Override
	public ListenableFutureTask<?> submit(final Runnable runnable) {
		final ListenableFutureTask<?> f = newTaskFor(runnable, null);
		execute(f);
		return f;
	}

}
