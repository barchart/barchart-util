package com.barchart.util.flow.provider;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.flow.api.Context;
import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Listener;
import com.barchart.util.flow.api.State;
import com.barchart.util.flow.api.Transit;

/**
 * State implementation.
 */
class StateBean<E extends Event<?>, S extends State<?>, A> implements
		Listener<E, S, A> {

	static class Builder<E extends Event<?>, S extends State<?>, A> implements
			State.Builder<E, S, A> {

		final S state;
		final FlowBean.Builder<E, S, A> flow;

		final Map<E, S> eventMap = new HashMap<E, S>();

		volatile Listener<E, S, A> listener;

		Builder(final S state, final FlowBean.Builder<E, S, A> flow) {
			this.state = state;
			this.flow = flow;
		}

		@Override
		public Event.Builder<E, S, A> on(final E event) {
			final EventBean.Builder<E, S, A> builder = flow.builder(event);
			builder.at(this);
			return builder;
		}

		@Override
		public State.Builder<E, S, A> listener(final Listener<E, S, A> listener) {
			this.listener = listener;
			return this;
		}

		StateBean<E, S, A> build() {
			return new StateBean<E, S, A>(this);
		}

	}

	static <E extends Event<?>, S extends State<?>, A> //
	Builder<E, S, A> newBuilder(final S state,
			final FlowBean.Builder<E, S, A> flow) {
		return new Builder<E, S, A>(state, flow);
	}

	static final Logger log = LoggerFactory.getLogger(StateBean.class);

	final Map<E, S> eventMap;

	final Listener<E, S, A> listener;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	StateBean(final Builder<E, S, A> builder) {

		eventMap = new EnumMap(builder.flow.eventClass);
		eventMap.putAll(builder.eventMap);

		listener = builder.listener;

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
			listener.enterError(transit, context, cause);
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
