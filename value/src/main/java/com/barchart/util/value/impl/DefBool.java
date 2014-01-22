/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

final class DefBool extends BaseBool {

	final boolean value;

	DefBool(final boolean value) {
		this.value = value;
	}

	@Override
	public final boolean value() {
		return value;
	}

}
