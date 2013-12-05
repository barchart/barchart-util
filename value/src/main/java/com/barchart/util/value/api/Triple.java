package com.barchart.util.value.api;

/**
 * A set of price, size and time.
 */
public interface Triple extends Tuple {

	@Override
	Price price();

	@Override
	Size size();

	Time time();
	
	@Override
	boolean isNull();
}
