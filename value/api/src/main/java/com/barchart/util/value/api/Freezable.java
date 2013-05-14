package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

/**
 * Semantic cloner.
 * <p>
 * Contract:
 * <li>T must be an interface
 * <li>return immutable value of T
 */
@ProviderType
public interface Freezable<T> {

	/**
	 * Create immutable value from a mutable state object.
	 * 
	 * @return immutable value of T
	 */
	T freeze();

}
