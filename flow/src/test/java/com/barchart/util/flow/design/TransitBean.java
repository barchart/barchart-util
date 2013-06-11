package com.barchart.util.flow.design;

import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.State;

/**
 * Transition implementation.
 */
class TransitBean<E extends Event<?>, S extends State<?>> implements
		Transit<E, S> {

	volatile E pastEvent;
	volatile S pastState;
	volatile E nextEvent;
	volatile S nextState;

	TransitBean() {
	}

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

	/**
	 * Replace past with next.
	 */
	void push(final E event, final S state) {
		pastEvent = nextEvent;
		pastState = nextState;
		nextEvent = event;
		nextState = state;
	}

	@Override
	public String toString() {
		return "{ pastEvent=" + pastEvent() + ", pastState=" + pastState()
				+ ", nextEvent=" + nextEvent() + ", nextState=" + nextState()
				+ " }";
	}

}
