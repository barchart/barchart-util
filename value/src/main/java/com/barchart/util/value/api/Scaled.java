/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.api;

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
public interface Scaled<T extends Scaled<T>> extends
		Comparable<T>, Existential {

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
	
	/**
	 * Returns this T rounded to a maximum significant digits (rounding half up)
	 * <p>
	 * Maximum significant digits must be a positive number or an Illegal Argument Exception 
	 * will be thrown.
	 * 
	 * @param maxSigDigits
	 * @return
	 */
	T round(int maxSigDigits);

	/** normalize: remove non significant zeros from mantissa, if any */
	T norm();

	/** flip sign */
	T neg();
	
	/** absolute value */
	T abs();

	//

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
	T mult(Scaled<?> factor) throws ArithmeticException;
	
	/** 
	 * Type safe multiplication for T, F with rounding of significant digits (rounding half up)
	 * <p>
	 * Note this method can still throw an ArithmeticException
	 * @param factor
	 * @param maxSigDigitsn the number of decimal places to round to
	 * @return
	 * @throws ArithmeticException
	 */
	T mult(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;

	/** type safe division for T, F */
	T div(Scaled<?> factor) throws ArithmeticException;
	
	/** 
	 * Type safe division for T, F with rounding of significant digits (rounding half up)
	 * <p>
	 * Note this method can still throw an ArithmeticException
	 * @param factor
	 * @param maxSigDigits the number of decimal places to round to
	 * @return
	 * @throws ArithmeticException
	 */	
	T div(Scaled<?> factor, int maxSigDigits) throws ArithmeticException;
	
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

	double asDouble();

	boolean greaterThan(T that);
	
	boolean greaterThanOrEquals(T that);

	boolean lessThan(T that);

	boolean lessThanOrEquals(T that);
	
	@Override
	boolean isNull();

}
