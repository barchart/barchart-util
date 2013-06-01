package com.barchart.util.value.provider;

import com.barchart.util.value.api.Factory;

/**
 * Static service provider.
 * <p>
 * This class is excluded from api at build time and must be available from the
 * provider at run time.
 */
public class FactoryProvider {

	/**
	 * Load a {@link Factory}.
	 */
	public static Factory instance() throws RuntimeException {
		throw new IllegalStateException("Build failure.");
	}

}
