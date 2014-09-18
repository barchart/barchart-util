package com.barchart.util.value.api;

/**
 * Immutable decimal value.
 * <p>
 * See <a href="http://en.wikipedia.org/wiki/Decimal">Decimal</a>
 */
public interface Decimal extends Scaled<Decimal>, Existential {

	@Override
	long mantissa();

	@Override
	int exponent();

	@Override
	boolean isZero();

	@Override
	Decimal scale(int exponent) throws ArithmeticException;

	@Override
	Decimal round(int maxSigDigits);

	@Override
	Decimal norm();

	@Override
	Decimal neg();

	@Override
	Decimal abs();

	@Override
	Decimal add(Decimal that) throws ArithmeticException;

	@Override
	Decimal add(long increment);

	@Override
	Decimal sub(Decimal that) throws ArithmeticException;

	@Override
	Decimal sub(long decrement);

	@Override
	Decimal mult(Scaled<?> factor) throws ArithmeticException;

	@Override
	Decimal mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;

	@Override
	Decimal mult(long factor) throws ArithmeticException;

	@Override
	Decimal div(Scaled<?> factor) throws ArithmeticException;

	@Override
	Decimal div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;

	@Override
	Decimal div(long factor) throws ArithmeticException;

	@Override
	long count(Decimal that) throws ArithmeticException;

	@Override
	boolean greaterThan(Decimal that);

	@Override
	boolean greaterThanOrEquals(Decimal that);

	@Override
	boolean lessThan(Decimal that);

	@Override
	boolean lessThanOrEquals(Decimal that);

	@Override
	int compareTo(Decimal that);

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	@Override
	boolean isNull();

	Decimal NULL = new Decimal() {

		@Override
		public boolean equalsScale(Decimal that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double asDouble() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long mantissa() {
			return 0;
			// throw new UnsupportedOperationException();
		}

		@Override
		public int exponent() {
			return 0;
			// throw new UnsupportedOperationException();
		}

		@Override
		public boolean isZero() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal scale(int exponent) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal round(int maxSigDigits) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal norm() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal neg() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal abs() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal add(Decimal that) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal add(long increment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal sub(Decimal that) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal sub(long decrement) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal mult(Scaled<?> factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal mult(long factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal div(Scaled<?> factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Decimal div(long factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public long count(Decimal that) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean greaterThan(Decimal that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean greaterThanOrEquals(Decimal that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean lessThan(Decimal that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean lessThanOrEquals(Decimal that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(Decimal that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}

	};

}
