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
 * API Provider Implementation.
 * <p>
 * Change in this class means:
 * <p>
 * API bundle is not affected.
 * <p>
 * API Consumer bundle is not affected.
 * <p>
 * API Provider bundle needs a release with MICRO version change.
 * 
 * 
 * @see <a
 *      href="https://github.com/barchart/barchart-documents/wiki/Version-Policy">Version
 *      Policy</a>
 */
@Documented
@Target({ ElementType.TYPE })
public @interface SemanticProviderImpl {

	String value() default "";

}
