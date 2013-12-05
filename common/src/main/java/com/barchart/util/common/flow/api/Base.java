package com.barchart.util.common.flow.api;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

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
