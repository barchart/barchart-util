package com.barchart.util.value.api;

import com.barchart.util.value.impl.FactoryImpl;

/**
 * Static service loader.
 */
public class FactoryLoader {

	static class Loader {

		public Factory produce() {
			return new FactoryImpl();
		}
		
	}

	/**
	 * Load {@link Factory} from external provider.
	 */
	public static Factory load() throws RuntimeException {
		return new Loader().produce();
	}

	private FactoryLoader() {
	}

}
