package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

/**
 * Semantic meaning of "null" or "missing" or "invalid" value.
 */
@ProviderType
public interface Existential {

	/**
	 * Check if this is "null" or "missing" or "invalid" value.
	 */
	boolean isNull();

}
