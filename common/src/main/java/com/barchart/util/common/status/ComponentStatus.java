package com.barchart.util.common.status;

/**
 * An interface describing the functional status of a specific application component.
 */
public interface ComponentStatus {

	/**
	 * The functional status of the component.
	 */
	enum Status {
		OK, WARNING, ERROR
	};

	/**
	 * The optionality of the component (i.e., how vital is it to correct application function.)
	 */
	enum Optionality {
		REQUIRED, OPTIONAL, IGNORED
	};

	/**
	 * The locality of the component (i.e., does it represent a remote dependency or local one.)
	 */
	enum Locality {
		LOCAL, REMOTE
	};

	/**
	 * The current functional status of the component.
	 */
	Status status();

	/**
	 * The optionality of the component. This can be overridden per-application in the status handler.
	 */
	Optionality optionality();

	/**
	 * The locality of the component.
	 */
	Locality locality();

	/**
	 * Component name.
	 */
	String name();

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
