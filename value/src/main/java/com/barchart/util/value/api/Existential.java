package com.barchart.util.value.api;

/**
 * Semantic meaning of "null" or "missing" or "invalid" value.
 */
public interface Existential {

	/**
	 * Check if this is "null" or "missing" or "invalid" value.
	 */
	boolean isNull();

}
