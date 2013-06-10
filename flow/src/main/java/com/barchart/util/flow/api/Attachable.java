package com.barchart.util.flow.api;

import aQute.bnd.annotation.ProviderType;

/**
 * State machine context attachment.
 */
@ProviderType
public interface Attachable<A> {

	/**
	 * Context attachment.
	 */
	A attachment();

}
