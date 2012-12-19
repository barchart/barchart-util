/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Basic implementation of {@link FutureCallback} based on {@link FutureTask}.
 * </p>
 * 
 * @author jeremy
 * @see FutureTask
 * @see FutureCallback
 * @param <E>
 *            The result type
 */
public class FutureCallbackTask<E> extends FutureTask<E> implements
		FutureCallback<E, FutureCallbackTask<E>> {

	private final static Logger log = LoggerFactory
			.getLogger(FutureCallbackTask.class);

	private final List<FutureListener<E>> listeners =
			new CopyOnWriteArrayList<FutureListener<E>>();
	private final Lock callbackLock = new ReentrantLock();

	/**
	 * Create a future result handler.
	 */
	public FutureCallbackTask(final Runnable r, final E value) {
		super(r, value);
	}

	/**
	 * Create a future result handler.
	 */
	public FutureCallbackTask(final Callable<E> c) {
		super(c);
	}

	@Override
	public FutureCallbackTask<E> addResultListener(
			final FutureListener<E> listener) {
		callbackLock.lock();
		try {
			listeners.add(listener);
			if (isDone()) {
				try {
					listener.resultAvailable(this);
				} catch (final Exception ex) {
					log.warn("Unhandled exception in callback", ex);
				}
			}
		} finally {
			callbackLock.unlock();
		}
		return this;
	}

	@Override
	public FutureCallbackTask<E> succeed(final E result_) {
		super.set(result_);
		return this;
	}

	@Override
	public FutureCallbackTask<E> fail(final Throwable error_) {
		super.setException(error_);
		return this;
	}

	@Override
	public E getUnchecked() {
		try {
			return get();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Notify listeners that a result is available.
	 */
	@Override
	protected void done() {
		callbackLock.lock();
		try {
			for (final FutureListener<E> l : listeners) {
				try {
					l.resultAvailable(this);
				} catch (final Exception ex) {
					log.warn("Unhandled exception in callback", ex);
				}
			}
		} finally {
			callbackLock.unlock();
		}
	}

	/**
	 * Log an error if this FutureCallbackTask is garbage collected before it
	 * has been called.
	 */
	@Override
	public void finalize() throws Throwable {
		try {
			if (!isDone()) {
				log.error("finalize() called on an incomplete FutureCallbackTask");
			}
		} catch (final Exception e) {
		} finally {
			super.finalize();
		}
	}

}
