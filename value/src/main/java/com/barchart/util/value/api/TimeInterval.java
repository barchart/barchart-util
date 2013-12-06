package com.barchart.util.value.api;

import com.barchart.util.value.ValueFactoryImpl;

public interface TimeInterval extends Existential {

	/** Special time interval value @see {isNull} */
	TimeInterval NULL = new ValueFactoryImpl().newTimeInterval(0, 0);
	
	Time start();

	Time stop();
	
	@Override
	boolean isNull();

}
