package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.FactoryImpl;

/**
 * Immutable price value.
 */
@NotMutable
@ProviderType
public interface Price extends Scaled<Price> {

	/** Special price value @see {isNull} */
	Price NULL = new FactoryImpl().newPrice(0,0);
	
	Price ZERO = new FactoryImpl().newPrice(0,0);
	
	Price ONE = new FactoryImpl().newPrice(1,0);

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
	Price mult(Scaled<?> factor) throws ArithmeticException;

	@Override
	Price mult(long factor) throws ArithmeticException;

	@Override
	Price div(Scaled<?> factor) throws ArithmeticException;

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
	boolean isNull();

}
