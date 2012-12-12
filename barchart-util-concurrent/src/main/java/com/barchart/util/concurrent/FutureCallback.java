package com.barchart.util.concurrent;

import java.util.concurrent.Future;

/**
 * <p>
 * Adds listener notification methods to {@link Future} to allow immediate
 * notification when a result is available.
 * </p>
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <p>
 * 
 * <pre>
 * FutureListener&lt;Employee&gt; listener = new FutureListener&lt;Employee&gt; {
 *   public void resultAvailable(Future&ltEmployee&gt; result) {
 *     System.out.println("Got deferred result: " + result.get());
 *   }
 * }
 * 
 * FutureCallback&lt;Employee&gt; result = myDataStore.getEmployee(id);
 * result.addResultListener(listener);
 * </pre>
 * 
 * </p>
 * 
 * <p>
 * In this example, getEmployee() returns a FutureCallback object that it will
 * later use to return a result asynchronously. When it has a result, it calls
 * result.succeed(Employee e), which in turn notifies any listeners registered
 * with result.addResultListener(). If the call fails, the owner may call
 * result.fail(Exception ex) to report the error. If a FutureCallback has
 * failed, any future calls to result.get() throw an ExecutionException with the
 * original exception as its cause.
 * </p>
 * 
 * <p>
 * This class is thread-safe.
 * </p>
 * 
 * @author jeremy
 * @see Future
 * @see FutureListener
 * @see FutureCallbackTask
 * @param <E>
 *            The result type
 */
public interface FutureCallback<V, T extends FutureCallback<V, T>> extends
		Future<V> {

	/**
	 * Register a listener for this deferred result. If the result is already
	 * available, the listener may be called synchronously, so you should
	 * generally avoid any blocking code in the callback.
	 * 
	 * @param listener
	 *            The listener object
	 */
	public T addResultListener(FutureListener<V> listener);

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
	public T succeed(V result);

	/**
	 * Notify listeners that an error occurred. Returns the current
	 * FutureCallbackTask instance to allow simple synchronous returns when an
	 * error has already occurred:<br />
	 * <code>return new FutureCallbackTask<Object>().fail(exception);</code>
	 * 
	 * @param error_
	 *            The deferred error
	 */
	public T fail(Throwable error);

	/**
	 * Get the return value, or null if a checked exception was caught.
	 */
	public V getUnchecked();

}
