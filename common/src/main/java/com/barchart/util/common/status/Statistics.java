package com.barchart.util.common.status;

/**
 * An interface describing the statistics of a component
 */
public interface Statistics {

	/**
	 * Stats name.
	 */
	String name();

	/**
	 * Stats count
	 */
	String count();

	/**
	 * Stats min
	 */
	String min();

	/**
	 * Stats max
	 */
	String max();

	/**
	 * Stats mean
	 */
	String mean();

}
