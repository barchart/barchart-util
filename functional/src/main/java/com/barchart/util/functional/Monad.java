package com.barchart.util.functional;

/**
 * 
 * Monad is the triple (T, η, μ) where
 * 
 * T is the Monad Functor η is the unit function that lifts an ordinary object
 * (a unit) into the Monad functor; and, μ is the join function that takes a
 * composed monad M (M a) and returns the simplified type of the composition, or
 * M a.
 * 
 * @author m-sokolowski
 * 
 * @param <M>
 * @param <A>
 */
public abstract class Monad<M, A> implements Joinable<M, A> {

	public <T> Monad<M, T> bind(Applicable<A, Monad<M, T>> f) throws Failure {

		Applicable<Monad<M, A>, Monad<M, Monad<M, T>>> a =
				(Applicable<Monad<M, A>, Monad<M, Monad<M, T>>>) fmap(f);

		Monad<M, Monad<M, T>> mmonad = a.apply(this);

		return (Monad<M, T>) mmonad.join();
	}

	public Monad<M, A> fail(String ex) throws Failure {
		throw new Failure(ex);
	}
}