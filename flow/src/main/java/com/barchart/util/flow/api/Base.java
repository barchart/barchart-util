package com.barchart.util.flow.api;

import aQute.bnd.annotation.ProviderType;

/**
 * JDK {@link Enum} base.
 */
@ProviderType
public interface Base {

	/**
	 * @see Enum#name()
	 */
	String name();

	/**
	 * @see Enum#ordinal()
	 */
	int ordinal();

}
