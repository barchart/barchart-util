/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * means can be used w/o special synchronization efforts
 */
@Documented
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface ThreadSafe {

	// Class<?> value();

	String rule() default "";

}
