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
 * <p>Basic implementation of {@link FutureCallback} based on {@link FutureTask}.</p>
 * 
 * @author jeremy
 * @see FutureTask
 * @see FutureCallback
 * @param <E> The result type
 */
public class FutureCallbackTask<E> extends FutureTask<E> implements FutureCallback<E> {
	
	private final static Logger log = LoggerFactory.getLogger(FutureCallbackTask.class);
	
	private final List<FutureListener<E>> listeners = new CopyOnWriteArrayList<FutureListener<E>>();
	private final Lock callbackLock = new ReentrantLock();
	
	/**
	 * Create a future result handler.
	 */
	public FutureCallbackTask(Runnable r, E value) {
		super(r, value);
	}
	
	/**
	 * Create a future result handler.
	 */
	public FutureCallbackTask(Callable<E> c) {
		super(c);
	}
	
	public FutureCallback<E> addResultListener(FutureListener<E> listener) {
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

	public FutureCallback<E> succeed(E result_) {
		super.set(result_);
		return this;
	}
	
	public FutureCallback<E> fail(Throwable error_) {
		super.setException(error_);
		return this;
	}
	
	/**
	 * Notify listeners that a result is available.
	 */
	protected void done() {
		callbackLock.lock();
		try {
			for (FutureListener<E> l : listeners)
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
	 * Log an error if this FutureCallbackTask is garbage collected before it has been
	 * called.
	 */
	@Override
	public void finalize() throws Throwable {
		try {
			if (!isDone())
				log.error("finalize() called on an incomplete FutureCallbackTask");
		} catch (Exception e) {
		} finally {
			super.finalize();
		}
	}

}
