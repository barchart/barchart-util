/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.values.api.SizeValue;

//16 bytes on 32 bit JVM
@NotMutable
final class DefSize extends BaseSize {

	private final long value;

	DefSize(long value) {
		this.value = value;
	}

	@Override
	public final long asLong() {
		return value;
	}

	@Override
	protected final SizeValue returnSize(long value) {
		return ValueBuilder.newSize(value);
	}

}
