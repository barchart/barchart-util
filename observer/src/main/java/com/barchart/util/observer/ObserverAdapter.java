package com.barchart.util.observer;

public class ObserverAdapter<T> implements Observer<T> {

	@Override
	public void onNext(final T next) {
	}

	@Override
	public void onError(final Throwable error) {
	}

	@Override
	public void onCompleted() {
	}

}
