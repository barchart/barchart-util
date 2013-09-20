package com.barchart.util.value.api;

import com.barchart.util.value.impl.FactoryImpl;

/**
 * Static service loader.
 */
public class FactoryLoader {

	static class Loader {

		/*protected Class<? extends Producer<Factory>> providerType() {
			return FactoryProvider.class;
		}

		protected Class<Factory> serviceType() {
			return Factory.class;
		}*/

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
