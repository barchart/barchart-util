package com.barchart.util.common.flow.api;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

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
