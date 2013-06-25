package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface TimeInterval extends Copyable<TimeInterval> {

	/** Special time interval value @see {isNull} */
	TimeInterval NULL = FactoryLoader.load().newTimeInterval(0, 0);
	
	// Name?
	// TimeZone?
	
	Time start();

	Time stop();

}
