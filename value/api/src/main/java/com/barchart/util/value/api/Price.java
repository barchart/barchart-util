package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

import com.barchart.util.anno.NotMutable;

/**
 * Immutable price value.
 */
@NotMutable
@ProviderType
public interface Price extends Scaled<Price, Decimal> {

	@Override
	long mantissa();

	@Override
	int exponent();

	@Override
	boolean isZero();

	@Override
	Price scale(int exponent) throws ArithmeticException;

	@Override
	Price norm();

	@Override
	Price neg();

	@Override
	Price add(Price that) throws ArithmeticException;

	@Override
	Price add(long increment);

	@Override
	Price sub(Price that) throws ArithmeticException;

	@Override
	Price sub(long decrement);

	@Override
	Price mult(Decimal factor) throws ArithmeticException;

	@Override
	Price mult(long factor) throws ArithmeticException;

	@Override
	Price div(Decimal factor) throws ArithmeticException;

	@Override
	Price div(long factor) throws ArithmeticException;

	@Override
	long count(Price that) throws ArithmeticException;

	@Override
	boolean greaterThan(Price that);

	@Override
	boolean lessThan(Price that);

	@Override
	int compareTo(Price that);

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	@Override
	<X extends Scaled<?, ?>> X asScaled();

}
