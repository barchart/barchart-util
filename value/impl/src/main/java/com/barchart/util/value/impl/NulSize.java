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

// 8 bytes on 32 bit JVM
@NotMutable
final class NulSize extends BaseSize {

	@Override
	public final long asLong() {
		return 0L;
	}

	@Override
	protected final Size returnSize(final long value) {
		return ValueBuilder.newSize(value);
	}

}
