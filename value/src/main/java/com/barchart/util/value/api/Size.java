package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.impl.ValueConst;

/**
 * Immutable size value.
 */
@NotMutable
@ProviderType
public interface Size extends Scaled<Size> {

	/** Special size value @see {isNull} */
	Size NULL = ValueConst.NULL_SIZE;
	
	@Override
	long mantissa();

	@Override
	int exponent();

	@Override
	boolean isZero();

	@Override
	Size scale(int exponent) throws ArithmeticException;

	@Override
	Size norm();

	@Override
	Size neg();

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
	Size mult(long factor) throws ArithmeticException;

	@Override
	Size div(Scaled<?> factor) throws ArithmeticException;

	@Override
	Size div(long factor) throws ArithmeticException;

	@Override
	long count(Size that) throws ArithmeticException;

	@Override
	boolean greaterThan(Size that);

	@Override
	boolean lessThan(Size that);

	@Override
	int compareTo(Size that);

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	@Override
	boolean isNull();

}
