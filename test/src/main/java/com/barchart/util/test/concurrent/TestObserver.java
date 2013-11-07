package com.barchart.util.test.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observer;

public class TestObserver<T> implements Observer<T> {

	public boolean completed = false;
	public Throwable error = null;
	public List<T> results = new ArrayList<T>();

	@Override
	public void onNext(final T next_) {
		results.add(next_);
	}

	@Override
	public void onError(final Throwable error_) {
		completed = true;
		error = error_;
	}

	@Override
	public void onCompleted() {
		completed = true;
	}

	public TestObserver<T> reset() {
		completed = false;
		error = null;
		results.clear();
		return this;
	}

	public TestObserver<T> sync() throws Exception {

		if (!completed) {
			CallableTest.waitFor(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return completed;
				}
			}, 5000);
		}

		return this;

	}

}