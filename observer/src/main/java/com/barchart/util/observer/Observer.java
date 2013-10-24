package com.barchart.util.observer;

// Copy of rxJava observer API for lightweight client-side use
public interface Observer<T> {

	public void onNext(T next);

	public void onError(Throwable error);

	public void onCompleted();

}
