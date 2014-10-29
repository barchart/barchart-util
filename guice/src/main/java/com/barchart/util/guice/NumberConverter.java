package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NumberConverter implements ValueConveter<Number> {

	public Collection<Object> convert(Number number) {
		List<Object> list = new ArrayList<Object>();
		list.add(number.longValue());
		list.add(number.intValue());
		list.add(number.shortValue());
		list.add(number.byteValue());
		list.add(number.doubleValue());
		list.add(number.floatValue());
		return list;
	}

}
