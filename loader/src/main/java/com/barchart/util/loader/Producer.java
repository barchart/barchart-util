package com.barchart.util.loader;

import aQute.bnd.annotation.ProviderType;

/**
 * Instance producer.
 */
@ProviderType
public interface Producer<T> {

	/**
	 * Produce same or new instance depending on context.
	 */
	T produce();

}
