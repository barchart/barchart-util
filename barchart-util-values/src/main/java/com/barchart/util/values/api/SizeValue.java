/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.api;

import com.barchart.util.anno.NotMutable;

@NotMutable
public interface SizeValue extends Value<SizeValue>, Comparable<SizeValue> {

	//

	// legacy compatibility
	@Deprecated
	int asInt();

	long asLong();
	
	//

	@Override
	int compareTo(SizeValue thatSize);

	@Override
	boolean equals(Object thatSize);

	//

	SizeValue add(SizeValue thatSize) throws ArithmeticException;

	SizeValue sub(SizeValue thatSize) throws ArithmeticException;

	SizeValue mult(long factor) throws ArithmeticException;

	SizeValue div(long factor) throws ArithmeticException;

	long count(SizeValue thatSize) throws ArithmeticException;

	boolean isZero();

}