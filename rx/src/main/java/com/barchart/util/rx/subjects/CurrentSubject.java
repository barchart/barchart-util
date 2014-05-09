package com.barchart.util.rx.subjects;

import java.util.concurrent.atomic.AtomicReference;

import rx.Observer;
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class CurrentSubject<T> extends Subject<T, T> {

	/**
	 * Create a new CurrentSubject.
	 */
	public static <T> CurrentSubject<T> create() {
		return create(PublishSubject.<T> create());
	}

	/**
	 * Create a new CurrentSubject based on an existing underlying subject.
	 */
	public static <T> CurrentSubject<T> create(final Subject<T, T> source) {

		// Most recent value
		final AtomicReference<T> current = new AtomicReference<T>();

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
		}, source, current);

		// This was the original suggestion by an RX developer, but unit tests
		// pass the same with the implementation above

		// intermediate.replay(new Func1<Observable<T>, Observable<T>>() {
		// public Observable<T> call(final Observable<T> o) {
		// return o.take(1);
		// }
		// }).subscribe(subscriber);

	}

	private final Observer<T> observer;
	private final AtomicReference<T> current;

	protected CurrentSubject(final OnSubscribe<T> onSubscribe, final Observer<T> observer,
			final AtomicReference<T> current) {

		super(onSubscribe);

		this.observer = observer;
		this.current = current;

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
		current.set(t);
		observer.onNext(t);
	}

	/**
	 * Return the current value of this subject. This may return null.
	 */
	public T peek() {
		return current.get();
	}

}
