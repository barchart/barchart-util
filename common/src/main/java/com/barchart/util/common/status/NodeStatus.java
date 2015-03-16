package com.barchart.util.common.status;

/**
 * An interface describing the functional status of a specific application component.
 */
public interface NodeStatus {

	/**
	 * The current functional status of the component.
	 */
	StatusType status();

	/**
	 * Node id.
	 */
	String id();

	/**
	 * Node name.
	 */
	String name();

	/**
	 * Node address.
	 */
	String address();

	/**
	 * Status message (optional).
	 */
	String message();

	/**
	 * The timestamp when the current status began.
	 */
	long timestamp();

	/**
	 * The number of consecutive checks the status has returned as non-OK.
	 */
	int errorCount();

	/**
	 * The number of times the component has changed status in the last <i>minutes</i> minutes.
	 *
	 * @return
	 */
	int flaps(int minutes);

}
