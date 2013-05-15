/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package temp;

import com.barchart.util.anno.NotMutable;

@NotMutable
public interface Size extends Comparable<Size> {

	//

	// legacy compatibility
	@Deprecated
	int asInt();

	long asLong();

	//

	@Override
	int compareTo(Size thatSize);

	@Override
	boolean equals(Object thatSize);

	//

	Size add(Size thatSize) throws ArithmeticException;

	Size sub(Size thatSize) throws ArithmeticException;

	Size mult(long factor) throws ArithmeticException;

	Size div(long factor) throws ArithmeticException;

	long count(Size thatSize) throws ArithmeticException;

	boolean isZero();

}