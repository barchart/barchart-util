package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Decimal<T extends Decimal<T>> extends Comparable<T>,
		Copyable<T> {

	/**
	 * a.k.a significand;
	 * 
	 * http://en.wikipedia.org/wiki/Significand
	 */
	long mantissa();

	/**
	 * a.k.a scale;
	 * 
	 * http://en.wikipedia.org/wiki/Exponentiation
	 */
	int exponent();

	/** mantissa == 0 */
	boolean isZero();

	/** change scale */
	T scale(int exponent) throws ArithmeticException;

	/** normalize: remove non significant zeros from mantissa, if any */
	T norm();

	/** change sign */
	T neg();

	/** type safe addition for T */
	T add(Decimal<?> that) throws ArithmeticException;

	// TODO add int, double methods?
	T add(long increment);

	/** type safe subtract for T */
	T sub(Decimal<?> that) throws ArithmeticException;

	T sub(long decrement);

	/** type safe multiply for T */
	T mult(Decimal<?> factor) throws ArithmeticException;

	T mult(long factor) throws ArithmeticException;

	/** type safe division for T */
	T div(Decimal<?> factor) throws ArithmeticException;

	T div(long factor) throws ArithmeticException;

	/** type safe division for T */
	long count(T that) throws ArithmeticException;

	boolean greaterThan(Decimal<?> that);

	boolean lessThan(Decimal<?> that);

	/** can be used for sorting */
	@Override
	int compareTo(T that);

	/** contract: must compare only to T */
	@Override
	boolean equals(Object that);

	/** can be used as keys */
	@Override
	int hashCode();

}
