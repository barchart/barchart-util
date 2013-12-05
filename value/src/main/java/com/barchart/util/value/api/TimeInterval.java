package com.barchart.util.value.api;

import com.barchart.util.value.FactoryImpl;

public interface TimeInterval extends Existential {

	/** Special time interval value @see {isNull} */
	TimeInterval NULL = new FactoryImpl().newTimeInterval(0, 0);
	
	Time start();

	Time stop();
	
	@Override
	boolean isNull();

}
