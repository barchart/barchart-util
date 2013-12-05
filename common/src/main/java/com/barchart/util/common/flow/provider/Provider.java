package com.barchart.util.common.flow.provider;

import com.barchart.util.common.flow.api.Event;
import com.barchart.util.common.flow.api.Flow;
import com.barchart.util.common.flow.api.State;

/**
 * State machine entry point.
 */
public class Provider {

	/**
	 * Create new flow builder.
	 */
	public static//
	<E extends Event<?>, S extends State<?>, A> //
	Flow.Builder<E, S, A> flowBuilder(final Class<E> eventClass,
			final Class<S> stateClass) {

		return FlowBean.newBuilder(eventClass, stateClass);

	}

}
