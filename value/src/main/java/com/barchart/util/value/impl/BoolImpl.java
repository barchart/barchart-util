/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

import com.barchart.util.value.api.Bool;

final class BoolImpl implements Bool {

	private final boolean value;

	BoolImpl(final boolean value) {
		this.value = value;
	}

	@Override
	public final boolean value() {
		return value;
	}

	//

	@Override
	public final int compareTo(final Bool that) {

		if (this.value() == that.value()) {
			return 0;
		}

		return this.value() ? 1 : -1;

	}

	@Override
	public final int hashCode() {
		return this.value() ? 1 : 0;
	}

	@Override
	public final boolean equals(Object that) {
		if (that instanceof Bool) {
			Bool thatBool = (Bool) that;
			return this.value() == thatBool.value();
		}
		return false;
	}

	@Override
	public final String toString() {
		return String.format("Bool > " + value());
	}

	@Override
	public final boolean isNull() {
		return this == Bool.NULL;
	}

}
