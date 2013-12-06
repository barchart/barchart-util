package com.barchart.util.flow.api;

/**
 * State machine context attachment.
 */
public interface Attachable<A> {

	/**
	 * Context attachment.
	 */
	A attachment();

}
