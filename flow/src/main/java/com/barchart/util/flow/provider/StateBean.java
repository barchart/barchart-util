package com.barchart.util.flow.provider;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Listener;
import com.barchart.util.flow.api.Point;
import com.barchart.util.flow.api.State;

/**
 * State implementation.
 */
class StateBean<E extends Event<?>, S extends State<?>, A> implements
		Listener<E, S, A> {

	static class Builder<E extends Event<?>, S extends State<?>, A> implements
			State.Builder<E, S, A> {

		final S state;

		final FlowBean.Builder<E, S, A> flowBuilder;

		final Map<E, S> transitionMap = new HashMap<E, S>();

		volatile Listener<E, S, A> listener;

		volatile FlowBean<E, S, A> flow;

		Builder(final S state, final FlowBean.Builder<E, S, A> flow) {
			this.state = state;
			this.flowBuilder = flow;
		}

		@Override
		public Event.Builder<E, S, A> on(final E event) {
			final EventBean.Builder<E, S, A> builder = flowBuilder
					.ensure(event);
			builder.at(this);
			return builder;
		}

		@Override
		public State.Builder<E, S, A> listener(final Listener<E, S, A> listener) {
			this.listener = listener;
			return this;
		}

		StateBean<E, S, A> build(final FlowBean<E, S, A> flow) {
			this.flow = flow;
			return new StateBean<E, S, A>(this);
		}

	}

	static <E extends Event<?>, S extends State<?>, A> //
	Builder<E, S, A> newBuilder(final S state,
			final FlowBean.Builder<E, S, A> flow) {
		return new Builder<E, S, A>(state, flow);
	}

	static final Logger log = LoggerFactory.getLogger(StateBean.class);

	final FlowBean<E, S, A> flow;

	/**
	 * Enum state.
	 */
	final S state;

	/**
	 * Event transition map for this state.
	 */
	final Map<E, S> transitionMap;

	final Listener<E, S, A> listener;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	StateBean(final Builder<E, S, A> builder) {

		flow = builder.flow;

		state = builder.state;

		transitionMap = new EnumMap(builder.flowBuilder.eventClass);
		transitionMap.putAll(builder.transitionMap);

		listener = builder.listener;

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

	@Override
	public String toString() {
		final StringBuilder text = new StringBuilder();
		final Set<Entry<E, S>> entrySet = transitionMap.entrySet();
		text.append("\n");
		text.append(state);
		for (final Entry<E, S> entry : entrySet) {
			text.append("\n\t");
			text.append(entry.getKey());
			text.append(" -> ");
			text.append(entry.getValue());
		}
		return text.toString();
	}

}
