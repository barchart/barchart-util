package com.barchart.util.common.flow.api;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

/**
 * State enums must implement this type and can not be empty.
 */
@ProviderType
public interface State<B extends Enum<B> & State<B>> extends Base {

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
