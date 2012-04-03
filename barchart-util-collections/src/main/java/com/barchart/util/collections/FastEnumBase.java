/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.collections;

class FastEnumBase<T extends BitSetEnum<T>> {

	protected final T[] enumValues;

	protected FastEnumBase(final T[] values) {
		enumValues = values;
		if (values.length > BitSetEnum.LIMIT) {
			throw new IllegalArgumentException("invalid use of : "
					+ FastEnumBase.class.getName() + " with : "
					+ values.getClass().getName() + " ; termintating");
		}
	}

}
