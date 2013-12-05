package com.barchart.util.common.flow.provider;

import com.barchart.util.common.flow.api.Event;
import com.barchart.util.common.flow.api.Point;
import com.barchart.util.common.flow.api.State;

/**
 * State coordinates implementation.
 */
class PointBean<E extends Event<?>, S extends State<?>> implements Point<E, S> {

	final E event;
	final S state;

	PointBean(final E event, final S state) {
		this.event = event;
		this.state = state;
	}

	@Override
	public E event() {
		return event;
	}

	@Override
	public S state() {
		return state;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(final Object other) {
		if (other instanceof PointBean) {
			final PointBean<E, S> that = (PointBean<E, S>) other;
			return this.event == that.event && this.state == that.state;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return event.hashCode() | state.hashCode();
	}

	@Override
	public String toString() {
		return "(" + event() + "," + state() + ")";
	}

}
