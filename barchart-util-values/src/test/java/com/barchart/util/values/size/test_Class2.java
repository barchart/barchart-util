/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.size;

import static com.barchart.util.values.size.JavaSize.*;

class test_Class2 extends test_Class1 {

	final static int SIZE = test_Class1.SIZE + 3 + REFERENCE_SIZE + OBJECT_SIZE
			+ 1 + 10 * 8;

	static float a33; // 0

	byte a3; // 1

	short a4; // 2

	double a5[] = new double[10]; // 1 ref + 1 obj + 10*8

}
