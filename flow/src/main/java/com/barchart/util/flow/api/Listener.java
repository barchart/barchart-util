package com.barchart.util.flow.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.bnd.annotation.ProviderType;

/**
 * State transition listener.
 */
@ProviderType
public interface Listener<E extends Event<?>, S extends State<?>, A> {

	/**
	 * State listener adapter.
	 */
	@ProviderType
	public class Adapter<E extends Event<?>, S extends State<?>, A> implements
			Listener<E, S, A> {

		protected final Logger log = LoggerFactory.getLogger(getClass());

		@Override
		public void enter(final Transit<E, S> transit,
				final Context<E, S, A> context) throws Exception {
		}

		@Override
		public void enterError(final Transit<E, S> transit,
				final Context<E, S, A> context, final Throwable cause) {
			log.error("Enter error.", cause);
		}

		@Override
		public void leave(final Transit<E, S> transit,
				final Context<E, S, A> context) throws Exception {
		}

		@Override
		public void leaveError(final Transit<E, S> transit,
				final Context<E, S, A> context, final Throwable cause) {
			log.error("Leave error.", cause);
		}

	}

	/**
	 * Flow entered state.
	 * 
	 * @param context
	 *            - next {@link Context#event()} and {@link Context#state()}
	 */
	void enter(Transit<E, S> transit, Context<E, S, A> context)
			throws Exception;

	/**
	 * Report {@link #enter(Transit, Context)} error.
	 */
	void enterError(Transit<E, S> transit, Context<E, S, A> context,
			Throwable cause);

	/**
	 * Flow left the state.
	 * 
	 * @param context
	 *            - past {@link Context#event()} and {@link Context#state()}
	 */
	void leave(Transit<E, S> transit, Context<E, S, A> context)
			throws Exception;

	/**
	 * Report {@link #leave(Transit, Context)} error.
	 */
	void leaveError(Transit<E, S> transit, Context<E, S, A> context,
			Throwable cause);

}
