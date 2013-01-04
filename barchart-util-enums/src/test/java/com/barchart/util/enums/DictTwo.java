/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.enums;

import com.barchart.util.enums.Dict;

class DictTwo<V> extends DictOne<V> {

	public static final Dict<String> TEST = NEW("string");

	public static final Dict<Integer> TWO = NEW("integer");

}