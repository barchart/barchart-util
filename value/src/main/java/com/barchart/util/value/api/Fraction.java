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
		public long mantissa() {return 0;}

		@Override
		public boolean isZero() {return false;}

		@Override
		public Decimal scale(int exponent) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal round(int maxSigDigits) {return Decimal.NULL;}

		@Override
		public Decimal norm() {return Decimal.NULL;}

		@Override
		public Decimal neg() {return Decimal.NULL;}

		@Override
		public Decimal abs() {return Decimal.NULL;}

		@Override
		public Decimal add(Decimal that) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal add(long increment) {return Decimal.NULL;}

		@Override
		public Decimal sub(Decimal that) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal sub(long decrement) {return Decimal.NULL;}

		@Override
		public Decimal mult(Scaled<?> factor) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal mult(long factor) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal div(Scaled<?> factor) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public Decimal div(long factor) throws ArithmeticException {return Decimal.NULL;}

		@Override
		public long count(Decimal that) throws ArithmeticException {return 0;}

		@Override
		public boolean greaterThan(Decimal that) {return false;}

		@Override
		public boolean greaterThanOrEquals(Decimal that) {return false;}

		@Override
		public boolean lessThan(Decimal that) {return false;}

		@Override
		public boolean lessThanOrEquals(Decimal that) {return false;}

		@Override
		public int compareTo(Decimal that) {return that.isNull() ? 0 : -1;}

		@Override
		public boolean equalsScale(Decimal that) {return false;}

		@Override
		public double asDouble() {return 0;}

		@Override
		public long base() {return 0;}

		@Override
		public int exponent() {return 0;}

		@Override
		public long numerator() {return 0;}

		@Override
		public long denominator() {return 0;}

		@Override
		public int decimalExponent() {return 0;}

		@Override
		public long decimalDenominator() {return 0;}

		@Override
		public int places() {return 0;}

		@Override
		public boolean isSmallerThan(Fraction that) {return false;}

		@Override
		public long priceFraction(Price price) {return 0;}

		@Override
		public long priceFraction(long mantissa, int exponent) {return 0;}

		@Override
		public long priceWhole(Price price) {return 0;}

		@Override
		public long priceWhole(long mantissa, int exponent) {return 0;}

		@Override
		public boolean isNull() {return true;}
		
	};

}
