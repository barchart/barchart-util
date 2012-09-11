package com.barchart.util.concurrent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * Groups a number of {@link FutureCallbackTask} results together, and executes
 * a single callback only after all FutureCallbacks have completed (succeeded or
 * failed.)
 * </p>
 * 
 * @author jeremy
 * @see FutureCallbackTask
 * @param <E>
 *            The deferred result type
 */
public class FutureCallbackList<E> implements Future<List<E>> {

	private final FutureListener<E> listener = new FutureListener<E>() {

		public void resultAvailable(Future<E> result_) {
			onResponse(result_);
		}

	};

	private final List<FutureListener<List<E>>> listeners = new CopyOnWriteArrayList<FutureListener<List<E>>>();
	private final List<E> responses = new CopyOnWriteArrayList<E>();
	private final List<? extends FutureCallback<E>> results;
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
	public FutureCallbackList(List<? extends FutureCallback<E>> results_) {
		results = results_;
		if (results_ == null || results_.size() == 0)
			fired = true;
		else {
			// Lock the semaphore until a result is available
			resultMonitor.acquireUninterruptibly();
			for (FutureCallback<E> r : results_)
				r.addResultListener(listener);
		}
	}

	/**
	 * Add a callback listener that will be called when all FutureCallbacks
	 * have completed. The callback will be passed a list of results
	 * collected from the FutureCallbacks. A null member may indicate that
	 * a FutureCallback failed, but does not guarantee it (since a
	 * FutureCallback
	 * callback could return a null value on purpose.)
	 * 
	 * @param callback
	 *            The object to notify of the deferred results
	 */
	public void addResultListener(FutureListener<List<E>> callback) {
		listeners.add(callback);
		if (fired)
			callback.resultAvailable(this);
	}

	/**
	 * Register a response from a FutureCallback.
	 */
	private void onResponse(Future<E> result) {
		if (fired)
			return;
		completed++;
		if (completed == results.size()) {
			for (FutureCallback<E> r : results) {
				try {
					responses.add(r.get());
				} catch (Exception e) {
					responses.add(null);
				}
			}
			fired = true;
			resultMonitor.release();
			for (FutureListener<List<E>> l : listeners)
				l.resultAvailable(this);
		}
	}

	public boolean cancel(boolean interrupt) {
		if (fired)
			return false;
		cancelled = true;
		for (FutureCallback<E> r : results)
			r.cancel(interrupt);
		return true;
	}

	public List<E> get() {
		if (!fired)
			resultMonitor.acquireUninterruptibly();
		return responses;
	}

	public List<E> get(long timeout, TimeUnit unit)
			throws InterruptedException, TimeoutException {
		if (!fired && !resultMonitor.tryAcquire(timeout, unit))
			throw new TimeoutException("Timed out waiting for result");
		return responses;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public boolean isDone() {
		return fired;
	}

}
