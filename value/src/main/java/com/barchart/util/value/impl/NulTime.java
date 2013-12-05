/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.value.impl;

// 8 bytes on 32 bit JVM
final class NulTime extends BaseTime {

	@Override
	public final long millisecond() {
		return 0L;
	}

}
