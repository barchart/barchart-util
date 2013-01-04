/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.collections;

import com.barchart.util.values.lang.ScaledDecimal;

public interface ScadecRingBuffer<K extends ScaledDecimal<K, ?>, V> extends
		RingBuffer<V> {

	int index(K key) throws ArrayIndexOutOfBoundsException;

	//

	K keyStep();

	K keyHead();

	K keyTail();

	//

	V get(K key) throws ArrayIndexOutOfBoundsException;

	void set(K key, V value) throws ArrayIndexOutOfBoundsException;

	//

	void setHead(K key, V value);

	void setTail(K key, V value);

}