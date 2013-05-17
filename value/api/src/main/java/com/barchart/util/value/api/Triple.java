package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

/**
 * A set of price, size and time.
 */
@ProviderType
public interface Triple extends Tuple {

	@Override
	Price price();

	@Override
	Size size();

	Time time();

}
