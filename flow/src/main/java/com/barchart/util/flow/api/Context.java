package com.barchart.util.flow.api;

import aQute.bnd.annotation.ProviderType;

/**
 * State machine context.
 */
@ProviderType
public interface Context<E extends Event<?>, S extends State<?>, A> extends
		Attachable<A> {

	/**
	 * Context builder.
	 */
	@ProviderType
	interface Builder<E extends Event<?>, S extends State<?>, A> {

		/**
		 * Optional initial past event. When omitted, will use flow default.
		 */
		Builder<E, S, A> initial(E event);

		/**
		 * Optional initial past state. When omitted, will use flow default.
		 */
		Builder<E, S, A> initial(S state);

		/**
		 * Build context with an attachment.
		 * 
		 * @throws RuntimeException
		 *             - When provided parameters are invalid.
		 */
		Context<E, S, A> build(A attahcment) throws RuntimeException;

	}

	@Override
	A attachment();

	/**
	 * Fire internal event.
	 */
	void fire(E event) throws RuntimeException;

	/**
	 * Current event.
	 */
	E event();

	/**
	 * Current state.
	 */
	S state();

}
