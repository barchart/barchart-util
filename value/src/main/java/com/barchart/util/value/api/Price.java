package com.barchart.util.value.api;

import com.barchart.util.value.ValueFactoryImpl;

/**
 * Immutable price value.
 */
public interface Price extends Scaled<Price> {

	Price ZERO = new ValueFactoryImpl().newPrice(0,0);
	
	Price ONE = new ValueFactoryImpl().newPrice(1,0);

	@Override
	long mantissa();

	@Override
	int exponent();

	@Override
	boolean isZero();

	@Override
	Price scale(int exponent) throws ArithmeticException;
	
	@Override
	Price round(int maxSigDigits);

	@Override
	Price norm();

	@Override
	Price neg();
	
	@Override
	Price abs();

	@Override
	Price add(Price that) throws ArithmeticException;

	@Override
	Price add(long increment);

	@Override
	Price sub(Price that) throws ArithmeticException;

	@Override
	Price sub(long decrement);

	@Override
	Price mult(Scaled<?> factor) throws ArithmeticException;
	
	@Override
	Price mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;

	@Override
	Price mult(long factor) throws ArithmeticException;
	
	@Override
	Price div(Scaled<?> factor) throws ArithmeticException;
	
	@Override
	Price div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;

	@Override
	Price div(long factor) throws ArithmeticException;
	
	@Override
	long count(Price that) throws ArithmeticException;

	@Override
	boolean greaterThan(Price that);

	@Override
	boolean greaterThanOrEquals(Price that);
	
	@Override
	boolean lessThan(Price that);
	
	@Override
	boolean lessThanOrEquals(Price that);

	@Override
	int compareTo(Price that);

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	@Override
	boolean isNull();
	
	Price NULL = new Price() {

		@Override
		public boolean equalsScale(Price that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double asDouble() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long mantissa() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int exponent() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isZero() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price scale(int exponent) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price round(int maxSigDigits) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price norm() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price neg() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price abs() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price add(Price that) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price add(long increment) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price sub(Price that) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price sub(long decrement) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price mult(Scaled<?> factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price mult(long factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price div(Scaled<?> factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Price div(long factor) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public long count(Price that) throws ArithmeticException {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean greaterThan(Price that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean greaterThanOrEquals(Price that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean lessThan(Price that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean lessThanOrEquals(Price that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(Price that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
