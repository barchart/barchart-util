package com.barchart.util.value.api;

import com.barchart.util.value.impl.ValueConst;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface TimeInterval extends Existential {

	/** Special time interval value @see {isNull} */
	TimeInterval NULL = ValueConst.NULL_TIME_INTERVAL;
	
	Time start();

	Time stop();
	
	@Override
	boolean isNull();

}
