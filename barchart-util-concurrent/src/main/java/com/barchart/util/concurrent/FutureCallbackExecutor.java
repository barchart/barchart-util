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

	public FutureCallbackExecutor(int poolSize, int maxPoolSize,
			long keepAlive, TimeUnit units, BlockingQueue<Runnable> queue) {
		super(poolSize, maxPoolSize, keepAlive, units, queue);
	}

	@Override
	protected <T> FutureCallbackTask<T> newTaskFor(Callable<T> callable) {
		return new FutureCallbackTask<T>(callable);
	}

	@Override
	protected <T> FutureCallbackTask<T> newTaskFor(Runnable runnable, T value) {
		return new FutureCallbackTask<T>(runnable, value);
	}

	@Override
	public <T> FutureCallback<T> submit(Callable<T> callable) {
		FutureCallbackTask<T> f = newTaskFor(callable);
		execute(f);
		return f;
	}

	@Override
	public <T> FutureCallback<T> submit(Runnable runnable, T value) {
		FutureCallbackTask<T> f = newTaskFor(runnable, value);
		execute(f);
		return f;
	}

	@Override
	public FutureCallback<?> submit(Runnable runnable) {
		FutureCallbackTask<?> f = newTaskFor(runnable, null);
		execute(f);
		return f;
	}

}
