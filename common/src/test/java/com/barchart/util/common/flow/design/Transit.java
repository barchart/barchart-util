package com.barchart.util.common.flow.design;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;
import com.barchart.util.common.flow.api.Event;
import com.barchart.util.common.flow.api.State;

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
