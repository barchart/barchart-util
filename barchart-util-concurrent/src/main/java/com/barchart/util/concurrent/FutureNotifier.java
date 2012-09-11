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
 * @author jeremy
 * 
 * @param <V>
 *            The result type
 */
public class FutureNotifier<V> implements FutureCallback<V>, FutureListener<V> {

	private final static Logger log = LoggerFactory
			.getLogger(FutureNotifier.class);

	/** Callback listeners */
	private final List<FutureListener<V>> listeners = new CopyOnWriteArrayList<FutureListener<V>>();
	private final Lock callbackLock = new ReentrantLock();

	/** Synchronization control for FutureNotifier */
	private final Sync sync;

	/**
	 * Creates a <tt>FutureNotifier</tt> that notifies any callbacks upon
	 * completion.
	 */
	public FutureNotifier() {
		sync = new Sync();
	}

	/**
	 * Creates a <tt>FutureNotifier</tt> that notifies any callbacks upon
	 * completion. If a runner thread is specified here, cancel() will attempt
	 * to interrupt it if requested.
	 */
	public FutureNotifier(Thread runner) {
		sync = new Sync(runner);
	}

	public boolean isCancelled() {
		return sync.innerIsCancelled();
	}

	public boolean isDone() {
		return sync.innerIsDone();
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return sync.innerCancel(mayInterruptIfRunning);
	}

	/**
	 * @throws CancellationException
	 *             {@inheritDoc}
	 */
	public V get() throws InterruptedException, ExecutionException {
		return sync.innerGet();
	}

	/**
	 * @throws CancellationException
	 *             {@inheritDoc}
	 */
	public V get(long timeout, TimeUnit unit) throws InterruptedException,
			ExecutionException, TimeoutException {
		return sync.innerGet(unit.toNanos(timeout));
	}

	/**
	 * Result listener for chaining callbacks together.
	 */
	@Override
	public void resultAvailable(Future<V> result) {
		try {
			succeed(result.get());
		} catch (ExecutionException e) {
			fail(e.getCause());
		} catch (Exception e) {
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
			for (FutureListener<V> l : listeners)
				try {
					l.resultAvailable(this);
				} catch (Exception ex) {
					log.warn("Unhandled exception in callback", ex);
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
	protected void set(V v) {
		sync.innerSet(v);
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
	protected void setException(Throwable t) {
		sync.innerSetException(t);
	}

	@Override
	public FutureNotifier<V> addResultListener(FutureListener<V> listener) {
		callbackLock.lock();
		try {
			listeners.add(listener);
			if (isDone()) {
				try {
					listener.resultAvailable(this);
				} catch (Exception ex) {
					log.warn("Unhandled exception in callback", ex);
				}
			}
		} finally {
			callbackLock.unlock();
		}
		return this;
	}

	@Override
	public FutureNotifier<V> fail(Throwable error) {
		setException(error);
		return this;
	}

	@Override
	public FutureNotifier<V> succeed(V result) {
		set(result);
		return this;
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

		Sync(Thread runner_) {
			runner = runner_;
		}

		private boolean ranOrCancelled(int state) {
			return (state & (RAN | CANCELLED)) != 0;
		}

		/**
		 * Implements AQS base acquire to succeed if ran or cancelled
		 */
		protected int tryAcquireShared(int ignore) {
			return innerIsDone() ? 1 : -1;
		}

		/**
		 * Implements AQS base release to always signal after setting final done
		 * status by nulling runner thread.
		 */
		protected boolean tryReleaseShared(int ignore) {
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
			if (getState() == CANCELLED)
				throw new CancellationException();
			if (exception != null)
				throw new ExecutionException(exception);
			return result;
		}

		V innerGet(long nanosTimeout) throws InterruptedException,
				ExecutionException, TimeoutException {
			if (!tryAcquireSharedNanos(0, nanosTimeout))
				throw new TimeoutException();
			if (getState() == CANCELLED)
				throw new CancellationException();
			if (exception != null)
				throw new ExecutionException(exception);
			return result;
		}

		void innerSet(V v) {
			for (;;) {
				int s = getState();
				if (s == RAN)
					return;
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

		void innerSetException(Throwable t) {
			for (;;) {
				int s = getState();
				if (s == RAN)
					return;
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

		boolean innerCancel(boolean mayInterruptIfRunning) {
			for (;;) {
				int s = getState();
				if (ranOrCancelled(s))
					return false;
				if (compareAndSetState(s, CANCELLED))
					break;
			}
			if (mayInterruptIfRunning) {
				Thread r = runner;
				if (r != null)
					r.interrupt();
			}
			releaseShared(0);
			done();
			return true;
		}

	}

}
