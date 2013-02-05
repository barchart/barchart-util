/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.dictenum;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.dictenum.e1.E1;
import com.barchart.util.dictenum.e11.E11;
import com.barchart.util.dictenum.e111.E111;
import com.barchart.util.dictenum.e12.E12;
import com.barchart.util.dictenum.e121.E121;
import com.barchart.util.dictenum.e122.E122;
import com.barchart.util.enums.Dict;
import com.barchart.util.enums.DictEnum;

public class TestDictEnum {

	@Test
	public void test0() {

		for (Dict<?> key : DictEnum.valuesFor(E1.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E122.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E11.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E12.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E111.class)) {
			System.out.println("# key : " + key);
		}

		System.out.println("########");

		for (Dict<?> key : DictEnum.valuesFor(E121.class)) {
			System.out.println("# key : " + key);
		}

		assertTrue(true);

	}

}
