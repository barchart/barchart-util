/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import temp.Size;

import com.barchart.util.anno.NotMutable;

//16 bytes on 32 bit JVM
@NotMutable
final class DefSize extends BaseSize {

	private final long value;

	DefSize(final long value) {
		this.value = value;
	}

	@Override
	public final long asLong() {
		return value;
	}

	@Override
	protected final Size returnSize(final long value) {
		return ValueBuilder.newSize(value);
	}

}