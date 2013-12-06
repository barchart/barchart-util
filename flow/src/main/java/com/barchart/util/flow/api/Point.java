package com.barchart.util.flow.api;


/**
 * State coordinates: a pair of event and state.
 */
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
