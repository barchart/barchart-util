package com.barchart.util.monad;

/**
 * Object setter *FUNCTION* in the Monadic domain
 * 
 * @author m-sokolowski
 * 
 * @param <Receiver>
 * @param <Datum>
 */
public abstract class MonadicSetter<Receiver, Datum> implements
		Applicable<Datum, Monad<Maybe, Receiver>> {

	protected MonadicSetter(Receiver r) {
		this.receiver = r;
	}

	@Override
	public final Monad<Maybe, Receiver> apply(Datum d) throws Failure {
		set(receiver, d);

		return Maybe.pure(receiver);
	}

	// subclasses implement with the particular set method being called
	protected abstract void set(Receiver r, Datum d);

	private final Receiver receiver;
}