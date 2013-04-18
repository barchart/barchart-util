/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.concurrent;

public class CancellableFutureNotifier<V, T extends LIstenableFuture<V, T>>
		extends FutureNotifierBase<V, T> {

	private LIstenableFuture<?, ?> parent;

	public CancellableFutureNotifier() {
		parent = null;
	}

	public CancellableFutureNotifier(final LIstenableFuture<?, ?> parent_) {
		parent = parent_;
	}

	public void setCancelCallback(final LIstenableFuture<?, ?> parent_) {
		parent = parent_;
	}

	@Override
	public boolean cancel(final boolean mayInterrupt) {
		if (parent != null) {
			parent.cancel(mayInterrupt);
		}
		return super.cancel(mayInterrupt);
	}

}
