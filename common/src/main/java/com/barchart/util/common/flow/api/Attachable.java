package com.barchart.util.common.flow.api;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

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
