package com.barchart.util.flow.provider;

import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.State;
import com.barchart.util.flow.api.Transit;

/**
 * Transition implementation.
 */
class TransitBean<E extends Event<?>, S extends State<?>> implements
		Transit<E, S> {

	final E pastEvent;
	final S pastState;
	final E nextEvent;
	final S nextState;

	TransitBean(final E pastEvent, final S pastState, final E nextEvent,
			final S nextState) {
		this.pastEvent = pastEvent;
		this.pastState = pastState;
		this.nextEvent = nextEvent;
		this.nextState = nextState;
	}

	@Override
	public E pastEvent() {
		return pastEvent;
	}

	@Override
	public S pastState() {
		return pastState;
	}

	@Override
	public E nextEvent() {
		return nextEvent;
	}

	@Override
	public S nextState() {
		return nextState;
	}

	@Override
	public String toString() {
		return "{ pastEvent=" + pastEvent() + ", pastState=" + pastState()
				+ ", nextEvent=" + nextEvent() + ", nextState=" + nextState()
				+ " }";
	}

}
