package com.barchart.util.value.api;

import com.barchart.util.value.ValueFactoryImpl;

public interface Bool extends Comparable<Bool>, Existential {

	/** Special time value @see {isNull} */
	Bool NULL = new ValueFactoryImpl().newBoolean(false);

	boolean value();

	@Override
	int compareTo(Bool that);

	@Override
	int hashCode();

	@Override
	boolean equals(Object that);

	@Override
	boolean isNull();
	
}
