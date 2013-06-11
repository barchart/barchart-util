package com.barchart.util.flow.provider;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		volatile StateBean.Builder<E, S, A> sourceBean;

		Builder(final E event, final FlowBean.Builder<E, S, A> flow) {
			this.event = event;
			this.flow = flow;
		}

		/**
		 * Provide transient source state for "to" clause.
		 */
		State.Builder<E, S, A> at(final StateBean.Builder<E, S, A> sourceBean) {
			if (sourceBean == null) {
				throw new IllegalStateException("Missing source bean.");
			}
			this.sourceBean = sourceBean;
			return sourceBean;
		}

		@Override
		public State.Builder<E, S, A> to(final S target) {

			if (target == null) {
				throw new NullPointerException("Missing target state.");
			}

			final State.Builder<E, S, A> targetBean = flow.ensure(target);
			if (targetBean == null) {
				throw new IllegalStateException("Missing target bean.");
			}

			if (sourceBean == null) {
				throw new IllegalStateException("Missing source bean.");
			}

			final S source = sourceBean.state;

			if (sourceBean.transitionMap.containsKey(event)) {
				throw new IllegalStateException("Duplicate event transition: "
						+ event + " at " + source);
			}

			if (this.transitionMap.containsKey(source)) {
				throw new IllegalStateException("Duplicate state transition: "
						+ source + "->" + target);
			}

			/** Map event->state transition for source/target state. */
			sourceBean.transitionMap.put(event, target);

			/** Map state->state transition for this event. */
			this.transitionMap.put(source, target);

			/** Continue source build. */
			return sourceBean;
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

	static final Logger log = LoggerFactory.getLogger(EventBean.class);

	/**
	 * Enum event.
	 */
	final E event;

	/**
	 * State transition map for this event.
	 */
	final Map<S, S> transitionMap;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	EventBean(final Builder<E, S, A> builder) {

		event = builder.event;

		transitionMap = new EnumMap(builder.flow.stateClass);
		transitionMap.putAll(builder.transitionMap);

	}

	@Override
	public String toString() {
		final StringBuilder text = new StringBuilder();
		final Set<Entry<S, S>> entrySet = transitionMap.entrySet();
		text.append("\n");
		text.append(event);
		for (final Entry<S, S> entry : entrySet) {
			text.append("\n\t");
			text.append(entry.getKey());
			text.append(" -> ");
			text.append(entry.getValue());
		}
		return text.toString();
	}

}
