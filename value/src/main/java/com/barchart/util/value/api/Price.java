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
			return false;
		}

		@Override
		public double asDouble() {
			return 0;
		}

		@Override
		public long mantissa() {
			return 0;
		}

		@Override
		public int exponent() {
			return 0;
		}

		@Override
		public boolean isZero() {
			return false;
		}

		@Override
		public Price scale(int exponent) throws ArithmeticException {
			return this;
		}

		@Override
		public Price round(int maxSigDigits) {
			return this;
		}

		@Override
		public Price norm() {
			return this;
		}

		@Override
		public Price neg() {
			return this;
		}

		@Override
		public Price abs() {
			return this;
		}

		@Override
		public Price add(Price that) throws ArithmeticException {
			return this;
		}

		@Override
		public Price add(long increment) {
			return this;
		}

		@Override
		public Price sub(Price that) throws ArithmeticException {
			return this;
		}

		@Override
		public Price sub(long decrement) {
			return this;
		}

		@Override
		public Price mult(Scaled<?> factor) throws ArithmeticException {
			return this;
		}

		@Override
		public Price mult(Scaled<?> factor, int maxSigDigits)
				throws ArithmeticException {
			return this;
		}

		@Override
		public Price mult(long factor) throws ArithmeticException {
			return this;
		}

		@Override
		public Price div(Scaled<?> factor) throws ArithmeticException {
			return this;
		}

		@Override
		public Price div(Scaled<?> factor, int maxSigDigits)
				throws ArithmeticException {
			return this;
		}

		@Override
		public Price div(long factor) throws ArithmeticException {
			return this;
		}

		@Override
		public long count(Price that) throws ArithmeticException {
			return 0;
		}

		@Override
		public boolean greaterThan(Price that) {
			return false;
		}

		@Override
		public boolean greaterThanOrEquals(Price that) {
			return false;
		}

		@Override
		public boolean lessThan(Price that) {
			return false;
		}

		@Override
		public boolean lessThanOrEquals(Price that) {
			return false;
		}

		@Override
		public int compareTo(Price that) {
			return 0;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
