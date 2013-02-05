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
 * 
 * 
 * means mutable, changing, live object, which can also be modified explicitly
 * 
 * */
@Documented
@Target(ElementType.TYPE)
public @interface Mutable {

	// Class<?> value();

}
