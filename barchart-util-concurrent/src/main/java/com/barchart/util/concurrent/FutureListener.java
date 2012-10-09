package com.barchart.util.concurrent;

import java.util.concurrent.Future;

/**
 * <p>Defines an interface for {@link FutureCallbackTask} callback listeners.
 * 
 * @author jeremy
 * @see FutureCallbackTask
 * @param <E> The type of object that the callback returns
 */
public interface FutureListener<E> {

	/**
	 * Called when the deferred task completes successfully.
	 * @param value The return value from the deferred task
	 */
	public void resultAvailable(Future<E> result);
	
}
