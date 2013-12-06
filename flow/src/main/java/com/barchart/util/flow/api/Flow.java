package com.barchart.util.flow.api;

import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Machine state flow event processor.
 */
public interface Flow<E extends Event<?>, S extends State<?>, A> {

	/**
	 * Flow builder. Must configure and build in single thread.
	 */
	interface Builder<E extends Event<?>, S extends State<?>, A> {

		/**
		 * State bean builder.
		 */
		State.Builder<E, S, A> at(S state);

		/**
		 * Optional initial context event. When omitted, will use first
		 * {@link Event#ordinal()} event.
		 */
		Builder<E, S, A> initial(E event);

		/**
		 * Optional terminal context event. Terminal event will unconditionally
		 * transfer machine to the terminal state.
		 */
		Builder<E, S, A> terminal(E event);

		/**
		 * Optional initial context state. When omitted, will use first
		 * {@link State#ordinal()} state.
		 */
		Builder<E, S, A> initial(S state);

		/**
		 * Optional terminal context state. Terminal state will discard any
		 * further events.
		 */
		Builder<E, S, A> terminal(S state);

		/**
		 * Optional global state listener.
		 */
		Builder<E, S, A> listener(Listener<E, S, A> listener);

		/**
		 * Enable flow debug log.
		 */
		Builder<E, S, A> debug(boolean isOn);

		/**
		 * Throw exceptions on unknown transitions, otherwise discard events.
		 */
		Builder<E, S, A> enforce(boolean isOn);

		/**
		 * Use thread-safe locking via {@link ReentrantLock}.
		 */
		Builder<E, S, A> locking(boolean isOn);

		/**
		 * Provide flow transition out-of-band executor. When omitted,
		 * {@link Flow#fire(Event, Context)} will be executed in-band.
		 */
		Builder<E, S, A> executor(Executor executor);

		/**
		 * Validate and produce the flow.
		 * 
		 * @throws RuntimeException - When provided parameters are invalid.
		 */
		Flow<E, S, A> build() throws RuntimeException;

	}

	/**
	 * New machine context builder.
	 */
	Context.Builder<E, S, A> contextBuilder();

	/**
	 * Fire external event.
	 */
	void fire(E event, Context<E, S, A> context) throws RuntimeException;

}
