package com.barchart.util.flow.provider;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Flow;
import com.barchart.util.flow.api.Listener;
import com.barchart.util.flow.api.Point;
import com.barchart.util.flow.api.State;

/**
 * State machine implementation.
 */
class FlowBean<E extends Event<?>, S extends State<?>, A> implements
		Flow<E, S, A>, Listener<E, S, A> {

	static class Builder<E extends Event<?>, S extends State<?>, A> implements
			Flow.Builder<E, S, A> {

		final Map<E, EventBean.Builder<E, S, A>> eventMap = new HashMap<E, EventBean.Builder<E, S, A>>();

		final Map<S, StateBean.Builder<E, S, A>> stateMap = new HashMap<S, StateBean.Builder<E, S, A>>();

		volatile boolean isDebug;
		volatile boolean isEnforce;
		volatile boolean isLocking;

		volatile E initialEvent;
		volatile S initialState;

		volatile E terminalEvent;
		volatile S terminalState;

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
		public Flow.Builder<E, S, A> debug(final boolean isOn) {
			this.isDebug = isOn;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> enforce(final boolean isOn) {
			this.isEnforce = isOn;
			return this;
		}

		@Override
		public Flow.Builder<E, S, A> locking(final boolean isOn) {
			this.isLocking = isOn;
			return this;
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

			if (terminalEvent != null && terminalState == null) {
				throw new IllegalStateException(
						"Missing terminal state for terminal event.");
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

	/**
	 * Global listener.
	 */
	final Listener<E, S, A> listener;

	final Class<E> eventClass;
	final Class<S> stateClass;

	final E initialEvent;
	final E terminalEvent;

	final S initialState;
	final S terminalState;

	final Executor executor;

	final PointMatrix<E, S> matrix;

	final boolean isDebug;
	final boolean isEnforce;
	final boolean isLocking;

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

		matrix = new PointMatrix<E, S>(eventClass, stateClass);

		isDebug = builder.isDebug;
		isEnforce = builder.isEnforce;
		isLocking = builder.isLocking;

	}

	@Override
	public Context.Builder<E, S, A> contextBuilder() {
		return ContextBean.newBuilder(this);
	}

	@Override
	public void fire(final E event, final Context<E, S, A> context) {

		debug("Fire on {} for {}", event, context);

		if (event == null) {
			throw new NullPointerException("Missing event.");
		}
		if (context == null) {
			throw new NullPointerException("Missing context.");
		}

		if (executor == null) {
			fireLocked(event, (ContextBean<E, S, A>) context);
		} else {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					fireLocked(event, (ContextBean<E, S, A>) context);
				}
			});
		}

	}

	final Lock lock = new ReentrantLock();

	/**
	 * Use mutual thread exclusion lock, if configured.
	 */
	void fireLocked(final E nextEvent, final ContextBean<E, S, A> context) {
		if (isLocking) {
			lock.lock();
			try {
				fireProcess(nextEvent, context);
			} finally {
				lock.unlock();
			}
		} else {
			fireProcess(nextEvent, context);
		}
	}

	/**
	 * Machine processing logic.
	 */
	void fireProcess(final E nextEvent, final ContextBean<E, S, A> context) {

		final E pastEvent = context.event();
		final S pastState = context.state();

		/** No events in termination state. */
		if (pastState == terminalState) {
			debug("Terminal state discard at {} on {} for {}", pastState,
					nextEvent, context);
			return;
		}

		/** Find past bean. */
		final StateBean<E, S, A> pastBean = stateMap.get(pastState);
		if (pastBean == null) {
			log.error("Missing past bean at {} on {} for {}", pastState,
					nextEvent, context);
			throw new IllegalStateException("Missing past bean.");
		}

		/** Find next state. */
		final S nextState;
		if (nextEvent == terminalEvent) {
			debug("Terminal event request at {} on {} for {}", pastState,
					nextEvent, context);
			nextState = terminalState;
		} else {
			nextState = pastBean.transitionMap.get(nextEvent);
		}
		if (nextState == null) {
			if (isEnforce) {
				log.error("Unknown next state at {} on {} for {}", pastState,
						nextEvent, context);
				throw new IllegalStateException("Unknown next state.");
			} else {
				debug("Discarded unkwown at {} on {} for {}", pastState,
						nextEvent, context);
				return;
			}
		}

		/** Find next bean. */
		final StateBean<E, S, A> nextBean = stateMap.get(nextState);
		if (nextBean == null) {
			log.error("Missing next bean at {} on {} for {}", pastState,
					nextEvent, context);
			throw new IllegalStateException("Missing next bean.");
		}

		/** Point cache lookup. */
		final Point<E, S> past = matrix.point(pastEvent, pastState);
		final Point<E, S> next = matrix.point(nextEvent, nextState);

		debug("Leave on {} from {}", nextEvent, context);

		/** Local leave. */
		try {
			pastBean.leave(past, next, context);
		} catch (final Throwable e) {
			pastBean.leaveError(past, next, context, e);
		}

		/** Global leave. */
		try {
			leave(past, next, context);
		} catch (final Throwable e) {
			leaveError(past, next, context, e);
		}

		context.event(nextEvent);
		context.state(nextState);

		debug("Enter on {} into {}", nextEvent, context);

		/** Local enter. */
		try {
			nextBean.enter(past, next, context);
		} catch (final Throwable e) {
			nextBean.enterError(past, next, context, e);
		}

		/** Global enter. */
		try {
			enter(past, next, context);
		} catch (final Throwable e) {
			enterError(past, next, context, e);
		}

	}

	@Override
	public void enter(final Point<E, S> past, final Point<E, S> next,
			final Context<E, S, A> context) throws Exception {
		if (listener == null) {
			return;
		}
		listener.enter(past, next, context);
	}

	@Override
	public void enterError(final Point<E, S> past, final Point<E, S> next,
			final Context<E, S, A> context, final Throwable cause) {
		if (listener == null) {
			return;
		}
		try {
			listener.enterError(past, next, context, cause);
		} catch (final Throwable e) {
			log.error("Enter error failure.", e);
		}
	}

	@Override
	public void leave(final Point<E, S> past, final Point<E, S> next,
			final Context<E, S, A> context) throws Exception {
		if (listener == null) {
			return;
		}
		listener.leave(past, next, context);
	}

	@Override
	public void leaveError(final Point<E, S> past, final Point<E, S> next,
			final Context<E, S, A> context, final Throwable cause) {
		if (listener == null) {
			return;
		}
		try {
			listener.leaveError(past, next, context, cause);
		} catch (final Throwable e) {
			log.error("Leave error failure.", e);
		}
	}

	/**
	 * Process flow debug log, when enabled.
	 */
	void debug(final String format, final Object... arguments) {
		if (isDebug && log.isDebugEnabled()) {
			log.debug(format, arguments);
		}
	}

	@Override
	public String toString() {
		final StringBuilder text = new StringBuilder();

		text.append("\n");
		text.append("---");

		text.append("\n\t debug=");
		text.append(isDebug);

		text.append("\n\t enforce=");
		text.append(isEnforce);

		final Set<Entry<E, EventBean<E, S, A>>> eventSet = eventMap.entrySet();
		for (final Entry<E, EventBean<E, S, A>> entry : eventSet) {
			text.append("\n");
			text.append("---");
			text.append(entry.getValue());
		}

		final Set<Entry<S, StateBean<E, S, A>>> stateSet = stateMap.entrySet();
		for (final Entry<S, StateBean<E, S, A>> entry : stateSet) {
			text.append("\n");
			text.append("---");
			text.append(entry.getValue());
		}

		text.append("\n");
		text.append("---");
		text.append("\n");

		return text.toString();
	}

}
