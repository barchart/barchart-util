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
 * means class or method that should not be used as yet
 * 
 * */
@Documented
@Target( { ElementType.TYPE, ElementType.METHOD })
public @interface NotYetImplemented {

	// Class<?> value();

}
