/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

/**
 * An implementation of FutureCallback that does not do any actual computation,
 * but just serves as a communication proxy between the executor and listeners.
 * 
 * @author jeremy
 * 
 * @param <V>
 *            The result type
 */
public class FutureNotifier<V> extends FutureNotifierBase<V, FutureNotifier<V>> {

	public static <P> FutureNotifier<P> success(final P result) {
		return new FutureNotifier<P>().succeed(result);
	}

	public static <P> FutureNotifier<P> failure(final Throwable error) {
		return new FutureNotifier<P>().fail(error);
	}

}