/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.common.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * API Package.
 * 
 * @see <a
 *      href="https://github.com/barchart/barchart-documents/wiki/Version-Policy">Version
 *      Policy</a>
 */
@Documented
@Target({ ElementType.PACKAGE })
@Retention(RetentionPolicy.CLASS)
public @interface SemanticAPI {

	String value() default "";

}
