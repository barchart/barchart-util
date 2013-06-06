package com.barchart.util.value.api;

import com.barchart.util.loader.BaseLoader;
import com.barchart.util.loader.Producer;
import com.barchart.util.value.provider.FactoryProvider;

/**
 * Static service loader.
 */
public class FactoryLoader {

	static class Loader extends BaseLoader<Factory> {

		@Override
		protected Class<? extends Producer<Factory>> providerType() {
			return FactoryProvider.class;
		}

		@Override
		protected Class<Factory> serviceType() {
			return Factory.class;
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
