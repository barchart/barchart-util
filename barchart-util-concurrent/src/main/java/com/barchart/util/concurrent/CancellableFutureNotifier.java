package com.barchart.util.concurrent;

public class CancellableFutureNotifier<V, T extends FutureCallback<V, T>>
		extends FutureNotifierBase<V, T> {

	private FutureCallback<?, ?> parent;

	public CancellableFutureNotifier() {
		parent = null;
	}

	public CancellableFutureNotifier(final FutureCallback<?, ?> parent_) {
		parent = parent_;
	}

	public void setCancelCallback(final FutureCallback<?, ?> parent_) {
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
