/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.collections.delete;

import com.barchart.util.values.api.PriceValue;

class PriceRingBuffer<V> extends ScadecRingBufferSimple<PriceValue, V> {

	public PriceRingBuffer(final int size, final PriceValue keyStep) {
		super(size, keyStep);
	}

}