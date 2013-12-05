package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.FactoryImpl;

/**
 * Immutable decimal value.
 * <p>
 * See <a href="http://en.wikipedia.org/wiki/Decimal">Decimal</a>
 */
@NotMutable
@ProviderType
public interface Decimal extends Scaled<Decimal>, Existential {
	
	/** Special decimal value @see {isNull} */
	Decimal NULL = new FactoryImpl().newDecimal(0, 0);
	
	@Override
	long mantissa();

	@Override
	int exponent();

	@Override
	boolean isZero();

	@Override
	Decimal scale(int exponent) throws ArithmeticException;

	@Override
	Decimal norm();

	@Override
	Decimal neg();

	@Override
	Decimal add(Decimal that) throws ArithmeticException;

	@Override
	Decimal add(long increment);

	@Override
	Decimal sub(Decimal that) throws ArithmeticException;

	@Override
	Decimal sub(long decrement);

	@Override
	Decimal mult(Scaled<?> factor) throws ArithmeticException;

	@Override
	Decimal mult(long factor) throws ArithmeticException;

	@Override
	Decimal div(Scaled<?> factor) throws ArithmeticException;

	@Override
	Decimal div(long factor) throws ArithmeticException;

	@Override
	long count(Decimal that) throws ArithmeticException;

	@Override
	boolean greaterThan(Decimal that);

	@Override
	boolean lessThan(Decimal that);

	@Override
	int compareTo(Decimal that);

	@Override
	boolean equals(Object that);

	@Override
	int hashCode();

	@Override
	boolean isNull();
	
}