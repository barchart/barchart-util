/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Groups a number of {@link FutureCallbackTask} results together, and executes
 * a single callback only after all FutureCallbacks have completed (succeeded or
 * failed.)
 * 
 * This class does not support direct access to succeed() or fail(), and will
 * throw and UnsupportedOperationException for them.
 * 
 * @author jeremy
 * @see FutureCallbackTask
 * @param <E>
 *            The deferred result type
 */
public class FutureCallbackList<E> implements
		FutureCallback<List<E>, FutureCallbackList<E>> {

	private final static Logger log = LoggerFactory
			.getLogger(FutureCallbackList.class);

	private final FutureListener<E> listener = new FutureListener<E>() {

		@Override
		public void resultAvailable(final Future<E> result_) {
			onResponse(result_);
		}

	};

	private final List<FutureListener<List<E>>> listeners =
			new CopyOnWriteArrayList<FutureListener<List<E>>>();
	private final List<E> responses = new CopyOnWriteArrayList<E>();
	private final List<? extends FutureCallback<E, ?>> results;
	private final Semaphore resultMonitor = new Semaphore(1);

	private volatile int completed = 0;
	private volatile boolean fired = false;
	private volatile boolean cancelled = false;

	/**
	 * Create a FutureCallbackList from the specified FutureCallback objects.
	 * 
	 * @param deferreds_
	 *            The list of deferred results to monitor
	 */
	public FutureCallbackList(
			final List<? extends FutureCallback<E, ?>> results_) {
		results = results_;
		if (results_ == null || results_.size() == 0) {
			fired = true;
		} else {
			// Lock the semaphore until a result is available
			resultMonitor.acquireUninterruptibly();
			for (final FutureCallback<E, ?> r : results_) {
				r.addResultListener(listener);
			}
		}
	}

	/**
	 * Add a callback listener that will be called when all FutureCallbacks have
	 * completed. The callback will be passed a list of results collected from
	 * the FutureCallbacks. A null member may indicate that a FutureCallback
	 * failed, but does not guarantee it (since a FutureCallback callback could
	 * return a null value on purpose.)
	 * 
	 * @param callback
	 *            The object to notify of the deferred results
	 */
	@Override
	public FutureCallbackList<E> addResultListener(
			final FutureListener<List<E>> callback) {
		listeners.add(callback);
		if (fired) {
			try {
				callback.resultAvailable(this);
			} catch (final Exception e) {
				log.warn("Unhandled exception in callback", e);
			}
		}
		return this;
	}

	/**
	 * Register a response from a FutureCallback.
	 */
	private void onResponse(final Future<E> result) {
		if (fired) {
			return;
		}
		completed++;
		if (completed == results.size()) {
			for (final FutureCallback<E, ?> r : results) {
				try {
					responses.add(r.get());
				} catch (final Exception e) {
					responses.add(null);
				}
			}
			fired = true;
			resultMonitor.release();
			for (final FutureListener<List<E>> l : listeners) {
				try {
					l.resultAvailable(this);
				} catch (final Exception e) {
					log.warn("Unhandled exception in callback", e);
				}
			}
		}
	}

	@Override
	public boolean cancel(final boolean interrupt) {
		if (fired) {
			return false;
		}
		cancelled = true;
		for (final FutureCallback<E, ?> r : results) {
			r.cancel(interrupt);
		}
		return true;
	}

	@Override
	public List<E> get() {
		if (!fired) {
			resultMonitor.acquireUninterruptibly();
		}
		return responses;
	}

	@Override
	public List<E> getUnchecked() {
		return get();
	}

	@Override
	public List<E> get(final long timeout, final TimeUnit unit)
			throws InterruptedException, TimeoutException {
		if (!fired && !resultMonitor.tryAcquire(timeout, unit)) {
			throw new TimeoutException("Timed out waiting for result");
		}
		return responses;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean isDone() {
		return fired;
	}

}
