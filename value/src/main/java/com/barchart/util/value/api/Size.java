package com.barchart.util.value.api;

import com.barchart.util.value.ValueFactoryImpl;

/**
 * Immutable size value.
 */
public interface Size extends Scaled<Size> {

	Size ZERO = new ValueFactoryImpl().newSize(0, 0);
	
	Size ONE = new ValueFactoryImpl().newSize(1, 0);
	
	@Override
	long mantissa();

	@Override
	int exponent();

	@Override
	boolean isZero();

	@Override
	Size scale(int exponent) throws ArithmeticException;
	
	@Override
	Size round(int maxSigDigits);

	@Override
	Size norm();

	@Override
	Size neg();

	@Override
	Size abs();
	
	@Override
	Size add(Size that) throws ArithmeticException;

	@Override
	Size add(long increment);

	@Override
	Size sub(Size that) throws ArithmeticException;

	@Override
	Size sub(long decrement);

	@Override
	Size mult(Scaled<?> factor) throws ArithmeticException;
	
	@Override
	Size mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;

	@Override
	Size mult(long factor) throws ArithmeticException;
	
	@Override
	Size div(Scaled<?> factor) throws ArithmeticException;
	
	@Override
	Size div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;

	@Override
	Size div(long factor) throws ArithmeticException;
	
	@Override
	long count(Size that) throws ArithmeticException;

	@Override
	boolean greaterThan(Size that);
	
	@Override
	boolean greaterThanOrEquals(Size that);

	@Override
	boolean lessThan(Size that);
	
	@Override
	boolean lessThanOrEquals(Size that);

	@Override
	int compareTo(Size that);

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	@Override
	boolean isNull();
	
	Size NULL = new Size() {

		@Override
		public boolean equalsScale(Size that) {
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
		public Size scale(int exponent) throws ArithmeticException {
			return this;
		}

		@Override
		public Size round(int maxSigDigits) {
			return this;
		}

		@Override
		public Size norm() {
			return this;
		}

		@Override
		public Size neg() {
			return this;
		}

		@Override
		public Size abs() {
			return this;
		}

		@Override
		public Size add(Size that) throws ArithmeticException {
			return this;
		}

		@Override
		public Size add(long increment) {
			return this;
		}

		@Override
		public Size sub(Size that) throws ArithmeticException {
			return this;
		}

		@Override
		public Size sub(long decrement) {
			return this;
		}

		@Override
		public Size mult(Scaled<?> factor) throws ArithmeticException {
			return this;
		}

		@Override
		public Size mult(Scaled<?> factor, int maxSigDigits)
				throws ArithmeticException {
			return this;
		}

		@Override
		public Size mult(long factor) throws ArithmeticException {
			return this;
		}

		@Override
		public Size div(Scaled<?> factor) throws ArithmeticException {
			return this;
		}

		@Override
		public Size div(Scaled<?> factor, int maxSigDigits)
				throws ArithmeticException {
			return this;
		}

		@Override
		public Size div(long factor) throws ArithmeticException {
			return this;
		}

		@Override
		public long count(Size that) throws ArithmeticException {
			return 0;
		}

		@Override
		public boolean greaterThan(Size that) {
			return false;
		}

		@Override
		public boolean greaterThanOrEquals(Size that) {
			return false;
		}

		@Override
		public boolean lessThan(Size that) {
			return false;
		}

		@Override
		public boolean lessThanOrEquals(Size that) {
			return false;
		}

		@Override
		public int compareTo(Size that) {
			return 0;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
