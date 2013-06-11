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
	public abstract class Adapter<E extends Event<?>, S extends State<?>, A>
			implements Listener<E, S, A> {

		protected final Logger log = LoggerFactory.getLogger(getClass());

		@Override
		public void enter(final Point<E, S> past, final Point<E, S> next,
				final Context<E, S, A> context) throws Exception {
		}

		@Override
		public void enterError(final Point<E, S> past, final Point<E, S> next,
				final Context<E, S, A> context, final Throwable cause) {
			log.error("Enter error.", cause);
		}

		@Override
		public void leave(final Point<E, S> past, final Point<E, S> next,
				final Context<E, S, A> context) throws Exception {
		}

		@Override
		public void leaveError(final Point<E, S> past, final Point<E, S> next,
				final Context<E, S, A> context, final Throwable cause) {
			log.error("Leave error.", cause);
		}

	}

	/**
	 * Flow entered next state.
	 * 
	 * @param context
	 *            - next {@link Context#event()} and {@link Context#state()}
	 */
	void enter(Point<E, S> past, Point<E, S> next, Context<E, S, A> context)
			throws Exception;

	/**
	 * Report {@link #enter()} error.
	 */
	void enterError(Point<E, S> past, Point<E, S> next,
			Context<E, S, A> context, Throwable cause);

	/**
	 * Flow left past state.
	 * 
	 * @param context
	 *            - past {@link Context#event()} and {@link Context#state()}
	 */
	void leave(Point<E, S> past, Point<E, S> next, Context<E, S, A> context)
			throws Exception;

	/**
	 * Report {@link #leave()} error.
	 */
	void leaveError(Point<E, S> past, Point<E, S> next,
			Context<E, S, A> context, Throwable cause);

}
