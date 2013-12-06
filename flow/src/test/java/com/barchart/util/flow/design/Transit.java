package com.barchart.util.flow.design;

import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.State;

/**
 * State transition vector.
 */
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
