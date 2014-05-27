package com.barchart.util.value.api;

public interface Fraction extends Decimal, Existential {
	
	long base();

	@Override
	int exponent();

	long numerator();

	long denominator();

	int decimalExponent();

	long decimalDenominator();

	int places();

	boolean isSmallerThan(Fraction that);

	long priceFraction(Price price);

	long priceFraction(long mantissa, int exponent);

	long priceWhole(Price price);

	long priceWhole(long mantissa, int exponent);
	
	@Override
	boolean isNull();
	
	Fraction NULL = new Fraction() {

		@Override
		public long mantissa() {
			throw new UnsupportedOperationException();
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
		public boolean equalsScale(Decimal that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public double asDouble() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long base() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int exponent() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long numerator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long denominator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int decimalExponent() {
			throw new UnsupportedOperationException();
		}

		@Override
		public long decimalDenominator() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int places() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isSmallerThan(Fraction that) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long priceFraction(Price price) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long priceFraction(long mantissa, int exponent) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long priceWhole(Price price) {
			throw new UnsupportedOperationException();
		}

		@Override
		public long priceWhole(long mantissa, int exponent) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
