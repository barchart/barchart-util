/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

import java.util.concurrent.Future;

/**
 * <p>
 * Defines an interface for {@link ListenableFutureTask} callback listeners.
 * 
 * @author jeremy
 * @see ListenableFutureTask
 * @param <E>
 *            The type of object that the callback returns
 */
public interface FutureCallback<E> {

	/**
	 * Called when the deferred task completes successfully.
	 * 
	 * @param value
	 *            The return value from the deferred task
	 */
	public void call(Future<E> result) throws Exception;

}
