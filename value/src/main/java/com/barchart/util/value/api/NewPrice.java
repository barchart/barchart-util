package com.barchart.util.value.api;

public interface NewPrice extends Existential, Comparable<NewPrice> {

	long mantissa();

	int exponent();

	boolean isZero();

	Price scale(int exponent) throws ArithmeticException;

	Price norm();

	Price neg();

	Price add(Price that) throws ArithmeticException;

	Price add(long increment);

	Price sub(Price that) throws ArithmeticException;

	Price sub(long decrement);

	Price mult(Decimal factor) throws ArithmeticException;

	Price mult(long factor) throws ArithmeticException;

	Price div(Decimal factor) throws ArithmeticException;

	Price div(long factor) throws ArithmeticException;

	long count(Price that) throws ArithmeticException;

	boolean greaterThan(Price that);

	boolean lessThan(Price that);

	@Override
	int compareTo(NewPrice that);

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	@Override
	boolean isNull();

	
}
