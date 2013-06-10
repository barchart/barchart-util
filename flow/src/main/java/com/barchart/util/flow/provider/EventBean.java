package com.barchart.util.flow.provider;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.State;

/**
 * Event implementation.
 */
class EventBean<E extends Event<?>, S extends State<?>, A> {

	static class Builder<E extends Event<?>, S extends State<?>, A> implements
			Event.Builder<E, S, A> {

		final E event;
		final FlowBean.Builder<E, S, A> flow;

		final Map<S, S> transitionMap = new HashMap<S, S>();

		volatile StateBean.Builder<E, S, A> source;

		Builder(final E event, final FlowBean.Builder<E, S, A> flow) {
			this.event = event;
			this.flow = flow;
		}

		/**
		 * Provide transient source state.
		 */
		State.Builder<E, S, A> at(final StateBean.Builder<E, S, A> source) {
			this.source = source;
			return source;
		}

		@Override
		public State.Builder<E, S, A> to(final S state) {
			if (source == null) {
				throw new IllegalStateException("Missing source state.");
			}
			if (transitionMap.containsKey(source.state)) {
				throw new IllegalStateException("Duplicate transition.");
			}
			transitionMap.put(source.state, state);
			return source;
		}

		EventBean<E, S, A> build() {
			return new EventBean<E, S, A>(this);
		}

	}

	/**
	 * Provide new event builder.
	 */
	static <E extends Event<?>, S extends State<?>, A> //
	Builder<E, S, A> newBuilder(final E event,
			final FlowBean.Builder<E, S, A> flow) {
		return new Builder<E, S, A>(event, flow);
	}

	/**
	 * State transition map.
	 */
	final Map<S, S> transitionMap;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	EventBean(final Builder<E, S, A> builder) {

		transitionMap = new EnumMap(builder.flow.stateClass);
		transitionMap.putAll(builder.transitionMap);

	}

	@Override
	public String toString() {
		return "TODO print config";
	}

}
