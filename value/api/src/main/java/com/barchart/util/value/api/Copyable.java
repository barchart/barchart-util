package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

// FIXME Remove?? 
@ProviderType
public interface Copyable<V> {

	V copy();

}
