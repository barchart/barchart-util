package com.barchart.util.rx.operators;

import rx.Observable.Operator;
import rx.Subscriber;

public class Each<R> implements Operator<R, Iterable<R>> {

	@Override
	public Subscriber<? super Iterable<R>> call(final Subscriber<? super R> subscriber) {

		return new Subscriber<Iterable<R>>(subscriber) {

			@Override
			public void onNext(final Iterable<R> iterable) {

				for (final R item : iterable) {

					if (subscriber.isUnsubscribed()) {
						return;
					}

					subscriber.onNext(item);

				}

			}

			@Override
			public void onError(final Throwable e) {
				subscriber.onError(e);
			}

			@Override
			public void onCompleted() {
				subscriber.onCompleted();
			}

		};

	}

	public static <T> Each<T> op() {
		return new Each<T>();
	}

}
