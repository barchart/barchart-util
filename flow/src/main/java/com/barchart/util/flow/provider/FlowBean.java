package com.barchart.util.flow.provider;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Flow;
import com.barchart.util.flow.api.Listener;
import com.barchart.util.flow.api.State;
import com.barchart.util.flow.api.Transit;

/**
 * State machine implementation.
 */
class FlowBean<E extends Event<?>, S extends State<?>, A> implements
		Flow<E, S, A>, Listener<E, S, A> {

	static class Builder<E extends Event<?>, S extends State<?>, A> implements
			Flow.Builder<E, S, A> {

		final Map<E, EventBean.Builder<E, S, A>> eventMap = new HashMap<E, EventBean.Builder<E, S, A>>();

		final Map<S, StateBean.Builder<E, S, A>> stateMap = new HashMap<S, StateBean.Builder<E, S, A>>();

		volatile E initialEvent;
		volatile E terminalEvent;

		volatile S initialState;
		volatile S terminalState;

		volatile boolean isTrace;

		volatile Executor executor;

		volatile Listener<E, S, A> listener;

		final Class<E> eventClass;
		final Class<S> stateClass;

		Builder(final Class<E> eventClass, final Class<S> stateClass) {

			if (!Enum.class.isAssignableFrom(eventClass)) {
				throw new IllegalStateException("Event type must extend Enum.");
			}
			if (!Enum.class.isAssignableFrom(stateClass)) {
				throw new IllegalStateException("State type must extend Enum.");
			}

			this.eventClass = eventClass;
			this.stateClass = stateClass;

		}

		EventBean.Builder<E, S, A> builder(final E event) {
			EventBean.Builder<E, S, A> builder = eventMap.get(event);
			if (builder == null) {
				builder = EventBean.newBuilder(event, this);
				eventMap.put(event, builder);
			}
			return builder;
		}

		StateBean.Builder<E, S, A> builder(final S state) {
			StateBean.Builder<E, S, A> builder = stateMap.get(state);
			if (builder == null) {
				builder = StateBean.newBuilder(state, this);
				stateMap.put(state, builder);
			}
			return builder;
		}

		@Override
		public State.Builder<E, S, A> at(final S state) {
			return builder(state);
		}

		@Override
		public Flow.Builder<E, S, A> initial(final E event) {
			this.initialEvent = event;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> terminal(final E event) {
			this.terminalEvent = event;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> initial(final S state) {
			this.initialState = state;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> terminal(final S state) {
			this.terminalState = state;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> trace(final boolean isOn) {
			this.isTrace = isOn;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> executor(final Executor executor) {
			this.executor = executor;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> listener(final Listener<E, S, A> listener) {
			this.listener = listener;
			return this;
		}

		@Override
		public Flow<E, S, A> build() {

			final E[] eventArray = Util.enumValues(eventClass);
			if (eventArray.length == 0) {
				throw new IllegalStateException("Event enum is empty.");
			}
			if (initialEvent == null) {
				initialEvent = eventArray[0];
			}

			final S[] stateArray = Util.enumValues(stateClass);
			if (stateArray.length == 0) {
				throw new IllegalStateException("State enum is empty.");
			}
			if (initialState == null) {
				initialState = stateArray[0];
			}

			final FlowBean<E, S, A> flow = new FlowBean<E, S, A>(this);
			validate(flow);
			return flow;
		}

	}

	/**
	 * Provide new state machine builder.
	 */
	static <E extends Event<?>, S extends State<?>, A> //
	Flow.Builder<E, S, A> newBuilder(final Class<E> eventClass,
			final Class<S> stateClass) {
		return new Builder<E, S, A>(eventClass, stateClass);
	}

	/**
	 * State machine configuration validator.
	 */
	static <E extends Event<?>, S extends State<?>, A> //
	void validate(final FlowBean<E, S, A> flow) {
		log.error("TODO");
	}

	static final Logger log = LoggerFactory.getLogger(FlowBean.class);

	/**
	 * Event store.
	 */
	final Map<E, EventBean<E, S, A>> eventMap;

	/**
	 * State store.
	 */
	final Map<S, StateBean<E, S, A>> stateMap;

	final Listener<E, S, A> listener;

	final Class<E> eventClass;
	final Class<S> stateClass;

	final E initialEvent;
	final E terminalEvent;

	final S initialState;
	final S terminalState;

	final Executor executor;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	FlowBean(final Builder<E, S, A> builder) {

		eventClass = builder.eventClass;
		stateClass = builder.stateClass;

		eventMap = new EnumMap(builder.eventClass);
		stateMap = new EnumMap(builder.stateClass);

		final Set<Entry<E, EventBean.Builder<E, S, A>>> eventSet = builder.eventMap
				.entrySet();

		/** Build events. */
		for (final Entry<E, EventBean.Builder<E, S, A>> entry : eventSet) {
			final E event = entry.getKey();
			final EventBean.Builder<E, S, A> eventBuilder = entry.getValue();
			eventMap.put(event, eventBuilder.build());
		}

		final Set<Entry<S, StateBean.Builder<E, S, A>>> stateSet = builder.stateMap
				.entrySet();

		/** Build states. */
		for (final Entry<S, StateBean.Builder<E, S, A>> entry : stateSet) {
			final S state = entry.getKey();
			final StateBean.Builder<E, S, A> stateBuilder = entry.getValue();
			stateMap.put(state, stateBuilder.build());
		}

		listener = builder.listener;

		initialEvent = builder.initialEvent;
		terminalEvent = builder.terminalEvent;

		initialState = builder.initialState;
		terminalState = builder.terminalState;

		executor = builder.executor;
	}

	@Override
	public Context.Builder<E, S, A> contextBuilder() {
		return ContextBean.newBuilder(this);
	}

	@Override
	public void fire(final E event, final Context<E, S, A> context) {

		if (event == null) {
			throw new NullPointerException("Missing next event.");
		}
		if (context == null) {
			throw new NullPointerException("Missing context.");
		}

		if (executor == null) {
			fire(event, (ContextBean<E, S, A>) context);
		} else {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					fire(event, (ContextBean<E, S, A>) context);
				}
			});
		}

	}

	/**
	 * Machine processing logic.
	 */
	void fire(final E event, final ContextBean<E, S, A> context) {

		final E pastEvent = context.event();
		final S pastState = context.state();

		/** No events after termination. */
		if (pastState == terminalState) {
			return;
		}

		final E nextEvent = event;

		final StateBean<E, S, A> pastBean = stateMap.get(pastState);
		if (pastBean == null) {
			throw new IllegalStateException("Unmapped past bean.");
		}

		final S nextState = pastBean.eventMap.get(nextEvent);
		if (nextState == null) {
			throw new IllegalStateException("Unmapped next event.");
		}

		final StateBean<E, S, A> nextBean = stateMap.get(nextState);
		if (nextBean == null) {
			throw new IllegalStateException("Unmapped next state.");
		}

		final Transit<E, S> transit = new TransitBean<E, S>(pastEvent,
				pastState, nextEvent, nextState);

		/** Local leave. */
		try {
			pastBean.leave(transit, context);
		} catch (final Throwable e) {
			pastBean.leaveError(transit, context, e);
		}

		/** Global leave. */
		try {
			leave(transit, context);
		} catch (final Throwable e) {
			leaveError(transit, context, e);
		}

		context.event(nextEvent);
		context.state(nextState);

		/** Local enter. */
		try {
			nextBean.enter(transit, context);
		} catch (final Throwable e) {
			nextBean.enterError(transit, context, e);
		}

		/** Global enter. */
		try {
			enter(transit, context);
		} catch (final Throwable e) {
			enterError(transit, context, e);
		}

	}

	@Override
	public void enter(final Transit<E, S> transit,
			final Context<E, S, A> context) throws Exception {
		if (listener == null) {
			return;
		}
		listener.enter(transit, context);
	}

	@Override
	public void enterError(final Transit<E, S> transit,
			final Context<E, S, A> context, final Throwable cause) {
		if (listener == null) {
			return;
		}
		try {
			listener.leaveError(transit, context, cause);
		} catch (final Throwable e) {
			log.error("Enter error failure.", e);
		}
	}

	@Override
	public void leave(final Transit<E, S> transit,
			final Context<E, S, A> context) throws Exception {
		if (listener == null) {
			return;
		}
		listener.leave(transit, context);
	}

	@Override
	public void leaveError(final Transit<E, S> transit,
			final Context<E, S, A> context, final Throwable cause) {
		if (listener == null) {
			return;
		}
		try {
			listener.leaveError(transit, context, cause);
		} catch (final Throwable e) {
			log.error("Leave error failure.", e);
		}
	}

	@Override
	public String toString() {
		return "TODO print config";
	}

}
