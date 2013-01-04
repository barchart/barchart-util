/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.enums;

import com.barchart.util.enums.Dict;
import com.barchart.util.enums.DictKey;

class DictBase<V> extends DictKey<V> {

	public static final Dict<String> X1 = NEW("string");

	public static final Dict<Integer> X2 = NEW("integer");

	public static final Dict<Float> X3 = NEW("float");

}