package com.barchart.util.value.api;

import java.net.URL;
import java.util.jar.Manifest;

import com.barchart.util.value.provider.FactoryProvider;

/**
 * Static service loader.
 */
public class FactoryLoader {

	/**
	 * Load {@link Factory} from external provider.
	 */
	public static Factory load() throws RuntimeException {
		try {
			return FactoryProvider.instance();
		} catch (final Throwable e) {
			throw new IllegalStateException("Provider not found.", e);
		}
	}

}
