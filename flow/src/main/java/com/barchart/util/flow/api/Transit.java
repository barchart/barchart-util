package com.barchart.util.flow.api;

import aQute.bnd.annotation.ProviderType;

/**
 * State transition vector.
 */
@ProviderType
public interface Transit<E extends Event<?>, S extends State<?>> {

	/**
	 * Past event.
	 */
	E pastEvent();

	/**
	 * Past state.
	 */
	S pastState();

	/**
	 * Next event.
	 */
	E nextEvent();

	/**
	 * Next state.
	 */
	S nextState();

}
