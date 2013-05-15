package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Copyable<V> {

	V copy();

}
