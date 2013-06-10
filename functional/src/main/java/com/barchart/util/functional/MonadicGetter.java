package com.barchart.util.functional;

/**
 * Object getter *FUNCTION* in the Monadic domain
 * 
 * @author m-sokolowski
 * 
 * @param <Receiver>
 * @param <Datum>
 */
public abstract class MonadicGetter<Container, Returned> implements
		Applicable<Container, Monad<Maybe, Returned>> {

	@Override
	public Monad<Maybe, Returned> apply(Container c) throws Failure {

		Maybe<Returned> answer = (Maybe<Returned>) Maybe.NOTHING;
		Returned result = get(c);

		// c inside the monad is guaranteed to be an instance
		// but result has NO guarantees!

		if (result != null)
			answer = Maybe.pure(result);

		// it must have a value... even if that value is NOTHING
		return answer;
	}

	protected abstract Returned get(Container c);
}