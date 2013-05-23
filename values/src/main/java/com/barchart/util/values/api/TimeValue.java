/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.api;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.barchart.util.anno.NotMutable;
import com.barchart.util.value.api.Time;

@NotMutable
public interface TimeValue extends Value<TimeValue>, Time {

	//

	/** in zone UTC */
	@Deprecated
	Date asDate();

	/** in zone UTC */
	DateTime asDateTime();

	DateTime asDateTime(DateTimeZone zone);

	//

	@Override
	int hashCode();

	@Override
	boolean equals(Object thatTime);

	//
}
