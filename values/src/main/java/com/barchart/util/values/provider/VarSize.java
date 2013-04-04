/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.provider;

import static com.barchart.util.values.provider.ValueBuilder.*;

import com.barchart.util.anno.Mutable;
import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.values.api.SizeValue;

//16 bytes on 32 bit JVM
@Mutable
@NotThreadSafe
final class VarSize extends BaseSize {

	private volatile long value;

	VarSize(final long value) {
		this.value = value;
	}

	@Override
	public final long asLong() {
		return value;
	}

	@Override
	protected final SizeValue returnSize(final long value) {
		this.value = value;
		return this;
	}

	@Override
	public final SizeValue freeze() {
		return newSize(value);
	}

	@Override
	public final boolean isFrozen() {
		return false;
	}

}
