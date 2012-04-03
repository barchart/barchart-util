/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
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
 * means that method is called once on object init/registration
 * 
 * */
@Documented
@Target( { ElementType.METHOD, ElementType.TYPE })
public @interface UsedOnce {

	// Class<?> value();

}
