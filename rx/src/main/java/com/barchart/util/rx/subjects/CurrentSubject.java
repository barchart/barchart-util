package com.barchart.util.rx.subjects;

import rx.Observer;
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class CurrentSubject<T> extends Subject<T, T> {

	public static <T> CurrentSubject<T> create() {

		// Source subject
		final PublishSubject<T> source = PublishSubject.create();

		// Single subscription for all subscribers that replays last item
		final ConnectableObservable<T> intermediate = source.replay(1);

		// Subscribe to source subject
		intermediate.connect();

		return new CurrentSubject<T>(new OnSubscribe<T>() {
			@Override
			public void call(final Subscriber<? super T> subscriber) {
				// Subscribe to first item of replay stream
				intermediate.take(1).subscribe(subscriber);
			}
		}, source);

		// This was the original suggestion by an RX developer, but unit tests
		// pass the same with the implementation above

		// intermediate.replay(new Func1<Observable<T>, Observable<T>>() {
		// public Observable<T> call(final Observable<T> o) {
		// return o.take(1);
		// }
		// }).subscribe(subscriber);

	}

	public Observer<T> observer;

	protected CurrentSubject(final OnSubscribe<T> onSubscribe, final Observer<T> observer) {
		super(onSubscribe);
		this.observer = observer;
	}

	@Override
	public void onCompleted() {
		observer.onCompleted();
	}

	@Override
	public void onError(final Throwable e) {
		observer.onError(e);
	}

	@Override
	public void onNext(final T t) {
		observer.onNext(t);
	}

}
