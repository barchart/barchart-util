package com.barchart.util.flow.provider;

import com.barchart.util.flow.api.Event;
import com.barchart.util.flow.api.Point;
import com.barchart.util.flow.api.State;

/**
 * State coordinates cache.
 */
class PointMatrix<E extends Event<?>, S extends State<?>> {

	/**
	 * Point cache: [event][state].
	 */
	final Object[][] matrix;

	PointMatrix(final Class<E> eventClass, final Class<S> stateClass) {

		Util.assertEnumType(eventClass);
		Util.assertEnumType(stateClass);

		final E[] eventArray = Util.enumValues(eventClass);
		final S[] stateArray = Util.enumValues(stateClass);

		matrix = new Object[eventArray.length][stateArray.length];

		for (final E event : eventArray) {
			for (final S state : stateArray) {
				matrix[event.ordinal()][state.ordinal()] = //
				new PointBean<E, S>(event, state);
			}
		}

	}

	/**
	 * Point cache lookup.
	 */
	@SuppressWarnings("unchecked")
	Point<E, S> point(final E event, final S state) {
		return (Point<E, S>) matrix[event.ordinal()][state.ordinal()];
	}

	int size() {
		int size = 0;
		for (int k = 0; k < matrix.length; k++) {
			final Object[] array = matrix[k];
			size += array.length;
		}
		return size;
	}

}
