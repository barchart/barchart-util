package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

/**
 * A pair of price and size.
 */
@ProviderType
public interface Tuple extends Existential {

	Price price();

	Size size();
	
	@Override
	boolean isNull();

}
