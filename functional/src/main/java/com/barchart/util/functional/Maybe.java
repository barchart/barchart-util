package com.barchart.util.functional;

/**
 * 
 * <p>
 * Maybe is useful for any semideterministic programming, that is: where
 * something may be true, or it may not be.
 * </p>
 * 
 * <p>
 * It alleviates for the use of endless checks for null pointers and allows
 * logic to not be intermixed with control statements
 * </p>
 * 
 * <p>
 * It resolves issues for problems like these endless chains of getter methods
 * in a service to data objects to set a result, but that often have nulls in
 * some values gotten along the chain.
 * </p>
 * 
 * <p>
 * i.e endless tests
 * </p>
 * 
 * <p>
 * A a = getA(); if(a != null) { B b = a.getB(); if(b != null) { C c = b.getC();
 * if(c != null) { foo.setD(c.getD()); } } }
 * </p>
 * 
 * 
 * @author m-sokolowski
 * 
 * @param <A>
 */
public abstract class Maybe<A> extends Monad<Maybe, A> {

	public static final Maybe<?> NOTHING = new Maybe() {

		@Override
		public String toString() {
			return "Nothing";
		}

		@Override
		public Object arg() throws Failure {
			throw new Failure("Cannot extract a value from Nothing.");
		}

		@Override
		public Functor join() throws Failure {
			return this;
		}

		@Override
		protected Maybe mbBind(Applicable f) {
			return this;
		}
	};

	public final static class Just<J> extends Maybe<J> {

		private final J _unit;

		public Just(J obj) {
			_unit = obj;
		}

		@Override
		public Maybe<?> join() throws Failure {
			try {
				return (Maybe<?>) _unit;
			} catch (ClassCastException ex) {
				throw new Failure("Joining on a flat structure!");
			}
		}

		@Override
		public String toString() {
			return "Just " + _unit;
		}

		@Override
		public J arg() throws Failure {
			return _unit;
		}

		@Override
		protected <T> Maybe<T> mbBind(Applicable<J, Monad<Maybe, T>> f)
				throws Failure {
			return (Maybe<T>) f.apply(_unit);
		}
	}

	public static <T> Maybe<T> pure(T x) {
		return new Just<T>(x);
	}

	@Override
	public Maybe<A> fail(String ex) throws Failure {
		return (Maybe<A>) NOTHING;
	}

	protected abstract <T> Maybe<T> mbBind(Applicable<A, Monad<Maybe, T>> arg)
			throws Failure;

	@Override
	public <T> Applicable<Maybe<A>, Maybe<T>> fmap(final Applicable<A, T> f)
			throws Failure {

		return new Applicable<Maybe<A>, Maybe<T>>() {

			@Override
			public Maybe<T> apply(Maybe<A> arg) throws Failure {

				Applicable<A, Monad<Maybe, T>> liFted =

				new Applicable<A, Monad<Maybe, T>>() {

					@Override
					public Maybe<T> apply(A arg) throws Failure {
						return Maybe.pure(f.apply(arg));
					}
				};

				return arg.mbBind(liFted);
			}
		};
	}
}
