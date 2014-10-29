package com.barchart.util.guice;

import java.util.Collection;

public interface ValueConveter<T> {

	public Collection<Object> convert(T value);

}
