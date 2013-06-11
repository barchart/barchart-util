package com.barchart.util.flow.api;

import aQute.bnd.annotation.ProviderType;

/**
 * State coordinates: a pair of event and state.
 */
@ProviderType
public interface Point<E extends Event<?>, S extends State<?>> {

	/**
	 * Event.
	 */
	E event();

	/**
	 * State.
	 */
	S state();

}
