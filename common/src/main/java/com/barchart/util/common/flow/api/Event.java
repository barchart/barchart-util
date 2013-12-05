package com.barchart.util.common.flow.api;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

/**
 * Event enums must implement this type and can not be empty.
 */
@ProviderType
public interface Event<B extends Enum<B> & Event<B>> extends Base {

	/**
	 * Event bean builder.
	 */
	@ProviderType
	interface Builder<E extends Event<?>, S extends State<?>, A> {

		/**
		 * Bind event to state.
		 */
		State.Builder<E, S, A> to(S state);

	}

	/**
	 * Name of the event.
	 */
	@Override
	String name();

	/**
	 * Identity of the event.
	 */
	@Override
	int ordinal();

}
