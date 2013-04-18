/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of FutureCallback that does not do any actual computation,
 * but just serves as a communication proxy between the executor and listeners.
 * 
 * This is a base class suitable for subclassing to return the correct object
 * type in succeed() and fail(). Ad-hoc notifiers can use the FutureNotifier
 * subclass to avoid additional parameterization.
 * 
 * @param <V>
 *            The result type
 * @param <T>
 *            The subclass being defined (for return value parameterization)
 */
public class FutureNotifierBase<V, T extends FutureCallback<V, T>> implements
		FutureCallback<V, T>, FutureListener<V> {

	private final static Logger log = LoggerFactory
			.getLogger(FutureNotifierBase.class);

	/** Callback listeners */
	private final List<FutureListener<V>> listeners =
			new CopyOnWriteArrayList<FutureListener<V>>();
	private final Lock callbackLock = new ReentrantLock();

	/** Synchronization control for FutureNotifier */
	private final Sync sync;

	/**
	 * Creates a <tt>FutureNotifier</tt> that notifies any callbacks upon
	 * completion.
	 */
	public FutureNotifierBase() {
		sync = new Sync();
	}

	/**
	 * Creates a <tt>FutureNotifier</tt> that notifies any callbacks upon
	 * completion. If a runner thread is specified here, cancel() will attempt
	 * to interrupt it if requested.
	 */
	public FutureNotifierBase(final Thread runner) {
		sync = new Sync(runner);
	}

	@Override
	public boolean isCancelled() {
		return sync.innerIsCancelled();
	}

	@Override
	public boolean isDone() {
		return sync.innerIsDone();
	}

	@Override
	public boolean cancel(final boolean mayInterruptIfRunning) {
		return sync.innerCancel(mayInterruptIfRunning);
	}

	/**
	 * @throws CancellationException
	 *             {@inheritDoc}
	 */
	@Override
	public V get() throws InterruptedException, ExecutionException {
		return sync.innerGet();
	}

	@Override
	public V getUnchecked() {
		try {
			return sync.innerGet();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * @throws CancellationException
	 *             {@inheritDoc}
	 */
	@Override
	public V get(final long timeout, final TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		return sync.innerGet(unit.toNanos(timeout));
	}

	/**
	 * Result listener for chaining callbacks together.
	 */
	@Override
	public void resultAvailable(final Future<V> result) {
		try {
			succeed(result.get());
		} catch (final ExecutionException e) {
			fail(e.getCause());
		} catch (final Exception e) {
			fail(e);
		}
	}

	/**
	 * Protected method invoked when this task transitions to state
	 * <tt>isDone</tt> (whether normally or via cancellation). The default
	 * implementation does nothing. Subclasses may override this method to
	 * invoke completion callbacks or perform bookkeeping. Note that you can
	 * query status inside the implementation of this method to determine
	 * whether this task has been cancelled.
	 */
	protected void done() {
		callbackLock.lock();
		try {
			for (final FutureListener<V> l : listeners) {
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
	 * Sets the result of this Future to the given value unless this future has
	 * already been set or has been cancelled. This method is invoked internally
	 * by the <tt>run</tt> method upon successful completion of the computation.
	 * 
	 * @param v
	 *            the value
	 */
	protected void set(final V v) {
		callbackLock.lock();
		try {
			if (isDone()) {
				throw new IllegalStateException("Future already completed");
			}
			sync.innerSet(v);
		} finally {
			callbackLock.unlock();
		}
	}

	/**
	 * Causes this future to report an <tt>ExecutionException</tt> with the
	 * given throwable as its cause, unless this Future has already been set or
	 * has been cancelled. This method is invoked internally by the <tt>run</tt>
	 * method upon failure of the computation.
	 * 
	 * @param t
	 *            the cause of failure
	 */
	protected void setException(final Throwable t) {
		callbackLock.lock();
		try {
			if (isDone()) {
				throw new IllegalStateException("Future already completed");
			}
			sync.innerSetException(t);
		} finally {
			callbackLock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T addResultListener(final FutureListener<V> listener) {
		callbackLock.lock();
		try {
			listeners.add(listener);
			if (isDone()) {
				try {
					listener.resultAvailable(this);
				} catch (final Exception ex) {
					log.warn("Unhandled exception in callback", ex);
				}
			} else if (listener instanceof CancellableFutureNotifier) {
				// MAGIC - register as parent for cancel calls
				((CancellableFutureNotifier<?, ?>) listener)
						.setCancelCallback(this);
			}
		} finally {
			callbackLock.unlock();
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T removeResultListener(final FutureListener<V> listener) {
		callbackLock.lock();
		try {
			listeners.remove(listener);
		} finally {
			callbackLock.unlock();
		}
		return (T) this;
	}

	public boolean hasListeners() {
		return listeners.size() > 0;
	}

	/**
	 * Notify listeners that an error occurred. Returns the current
	 * FutureCallbackTask instance to allow simple synchronous returns when an
	 * error has already occurred:<br />
	 * <code>return new FutureCallbackTask<Object>().fail(exception);</code>
	 * 
	 * @param error_
	 *            The deferred error
	 */
	@SuppressWarnings("unchecked")
	public T fail(final Throwable error) {
		setException(error);
		return (T) this;
	}

	/**
	 * Notify listeners that a result is available. Returns the current
	 * FutureCallbackTask instance to allow simple synchronous returns when the
	 * result is already available:<br />
	 * <code>return new FutureCallbackTask<Object>().succeed(result);</code>
	 * 
	 * @param result_
	 *            The deferred result
	 * @return This FutureCallbackTask object (for chaining calls)
	 */
	@SuppressWarnings("unchecked")
	public T succeed(final V result) {
		set(result);
		return (T) this;
	}

	/**
	 * Synchronization control for FutureNotifier. Note that this must be a
	 * non-static inner class in order to invoke the protected <tt>done</tt>
	 * method. For clarity, all inner class support methods are same as outer,
	 * prefixed with "inner".
	 * 
	 * Uses AQS sync state to represent run status
	 */
	private final class Sync extends AbstractQueuedSynchronizer {
		private static final long serialVersionUID = -7828117401763700385L;

		/** State value representing that task ran */
		private static final int RAN = 1;
		/** State value representing that task was cancelled */
		private static final int CANCELLED = 2;

		/** The result to return from get() */
		private V result = null;
		/** The exception to throw from get() */
		private Throwable exception = null;

		/**
		 * The thread running task. When nulled after set/cancel, this indicates
		 * that the results are accessible. Must be volatile, to ensure
		 * visibility upon completion.
		 */
		private volatile Thread runner = null;

		Sync() {
		}

		Sync(final Thread runner_) {
			runner = runner_;
		}

		private boolean ranOrCancelled(final int state) {
			return (state & (RAN | CANCELLED)) != 0;
		}

		/**
		 * Implements AQS base acquire to succeed if ran or cancelled
		 */
		@Override
		protected int tryAcquireShared(final int ignore) {
			return innerIsDone() ? 1 : -1;
		}

		/**
		 * Implements AQS base release to always signal after setting final done
		 * status by nulling runner thread.
		 */
		@Override
		protected boolean tryReleaseShared(final int ignore) {
			runner = null;
			return true;
		}

		boolean innerIsCancelled() {
			return getState() == CANCELLED;
		}

		boolean innerIsDone() {
			return ranOrCancelled(getState()) && runner == null;
		}

		V innerGet() throws InterruptedException, ExecutionException {
			acquireSharedInterruptibly(0);
			if (getState() == CANCELLED) {
				throw new CancellationException();
			}
			if (exception != null) {
				throw new ExecutionException(exception);
			}
			return result;
		}

		V innerGet(final long nanosTimeout) throws InterruptedException,
				ExecutionException, TimeoutException {
			if (!tryAcquireSharedNanos(0, nanosTimeout)) {
				throw new TimeoutException();
			}
			if (getState() == CANCELLED) {
				throw new CancellationException();
			}
			if (exception != null) {
				throw new ExecutionException(exception);
			}
			return result;
		}

		void innerSet(final V v) {
			for (;;) {
				final int s = getState();
				if (s == RAN) {
					return;
				}
				if (s == CANCELLED) {
					// aggressively release to set runner to null,
					// in case we are racing with a cancel request
					// that will try to interrupt runner
					releaseShared(0);
					return;
				}
				if (compareAndSetState(s, RAN)) {
					result = v;
					releaseShared(0);
					done();
					return;
				}
			}
		}

		void innerSetException(final Throwable t) {
			for (;;) {
				final int s = getState();
				if (s == RAN) {
					return;
				}
				if (s == CANCELLED) {
					// aggressively release to set runner to null,
					// in case we are racing with a cancel request
					// that will try to interrupt runner
					releaseShared(0);
					return;
				}
				if (compareAndSetState(s, RAN)) {
					exception = t;
					result = null;
					releaseShared(0);
					done();
					return;
				}
			}
		}

		boolean innerCancel(final boolean mayInterruptIfRunning) {
			for (;;) {
				final int s = getState();
				if (ranOrCancelled(s)) {
					return false;
				}
				if (compareAndSetState(s, CANCELLED)) {
					break;
				}
			}
			if (mayInterruptIfRunning) {
				final Thread r = runner;
				if (r != null) {
					r.interrupt();
				}
			}
			releaseShared(0);
			done();
			return true;
		}

	}

}
