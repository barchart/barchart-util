package com.barchart.util.values.api;

public interface TimeInterval extends Value<TimeInterval> {

	TimeValue start();
	
	long startAsMillis();
	
	TimeValue stop();
	
	long stopAsMillis();
	
}
