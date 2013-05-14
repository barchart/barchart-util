package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Tuple {

	Price price();

	Size size();

}
