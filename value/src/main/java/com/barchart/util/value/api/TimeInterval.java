package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

import com.barchart.util.value.FactoryImpl;

@ProviderType
public interface TimeInterval extends Existential {

	/** Special time interval value @see {isNull} */
	TimeInterval NULL = new FactoryImpl().newTimeInterval(0, 0);
	
	Time start();

	Time stop();
	
	@Override
	boolean isNull();

}
