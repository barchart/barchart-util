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
 * API Provider Interface.
 * <p>
 * Change in this interface means:
 * <p>
 * API bundle needs a release with MINOR version change.
 * <p>
 * API Consumer bundle is not affected.
 * <p>
 * API Provider bundle needs a release with MINOR version change.
 * 
 * @see https://github.com/barchart/barchart-documents/wiki/Version-Policy
 */
@Documented
@Target({ ElementType.TYPE })
public @interface SemanticProviderApi {

	String value() default "";

}
