/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.enums;

public interface ParamEnum2D<V, T extends ParamEnum2D<V, T>> extends
		ParamEnum<V, T> {

	//
	int row();

	int col();

}
