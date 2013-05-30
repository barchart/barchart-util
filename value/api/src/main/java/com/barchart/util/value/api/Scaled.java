/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

/**
 * Represents type safe base 10 floating point
 * <p>
 * See http://en.wikipedia.org/wiki/Floating_point
 * <p>
 * 
 * @param T
 *            - Derived scaled type.
 * @param F
 *            - Permitted factor type.
 */
@ProviderType
public interface Scaled<T extends Scaled<T, F>, F extends Scaled<F, ?>> extends
		Comparable<T> {

	//

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

	//

	/** mantissa == 0 */
	boolean isZero();

	/** compare scales of two numbers */
	boolean equalsScale(T that);

	//

	/** change scale */
	T scale(int exponent) throws ArithmeticException;

	/** normalize: remove non significant zeros from mantissa, if any */
	T norm();

	/** change sign */
	T neg();

	//

	// TODO add int, double methods?
	/** type safe addition for long */
	T add(long increment);

	/** type safe addition for T */
	T add(T that) throws ArithmeticException;

	/** type safe subtract for long */
	T sub(long decrement);

	/** type safe subtract for T */
	T sub(T that) throws ArithmeticException;

	/** type safe multiply for T */
	T mult(long factor) throws ArithmeticException;

	/** type safe division for T */
	T div(long factor) throws ArithmeticException;

	/** type safe division for T */
	long count(T that) throws ArithmeticException;

	//

	/** type safe multiply for T, F */
	T mult(F factor) throws ArithmeticException;

	/** type safe division for T, F */
	T div(F factor) throws ArithmeticException;

	//

	/** can be used for sorting */
	@Override
	int compareTo(T that);

	/** contract: must compare only to T */
	@Override
	boolean equals(Object that);

	/** can be used as keys */
	@Override
	int hashCode();

	//

	/** TODO */
	double asDouble();

	/** TODO */
	boolean greaterThan(T that);

	/** TODO */
	boolean lessThan(T that);

	//

	/** Convert to another scaled type. */
	<X extends Scaled<?, ?>> X asScaled();

}
