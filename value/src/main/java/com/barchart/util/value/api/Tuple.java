package com.barchart.util.value.api;

/**
 * A pair of price and size.
 */
public interface Tuple extends Existential {

	Price price();

	Size size();
	
	@Override
	boolean isNull();

}
