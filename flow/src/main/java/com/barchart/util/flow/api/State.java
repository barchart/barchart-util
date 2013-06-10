package com.barchart.util.flow.api;

import aQute.bnd.annotation.ProviderType;

/**
 * State enums must implement this type.
 */
@ProviderType
public interface State<B extends Enum<B>> extends Base {

	/**
	 * State bean builder.
	 */
	@ProviderType
	interface Builder<E extends Event<?>, S extends State<?>, A> {

		/**
		 * Bind state to event.
		 */
		Event.Builder<E, S, A> on(E event);

		/**
		 * Optional state listener.
		 */
		State.Builder<E, S, A> listener(Listener<E, S, A> listener);

	}

	/**
	 * Name of the state.
	 */
	@Override
	String name();

	/**
	 * Identity of the state.
	 */
	@Override
	int ordinal();

}
