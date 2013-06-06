package com.barchart.util.loader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service loader template.
 * <p>
 * Attempt to load service from multiple sources.
 * <li>user static provider
 * <li>java service loader
 */
public abstract class BaseLoader<T> implements Producer<T> {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public T produce() throws RuntimeException {

		/**
		 * Try to load from static provider.
		 */
		Throwable providerCause = null;
		try {
			final T instance = providerInstance(providerType());
			log.debug("Resolved from static provider: {}", instance);
			return instance;
		} catch (final Throwable e) {
			providerCause = e;
		}

		/**
		 * Try to load from service loader.
		 */
		Throwable serviceCause = null;
		try {
			final T instance = serviceInstance(serviceType());
			log.debug("Resolved from service loader: {}", instance);
			return instance;
		} catch (final Throwable e) {
			serviceCause = e;
		}

		/**
		 * Report failure.
		 */
		log.error("1. Static provider failure.", providerCause);
		log.error("2. Service loader failure.", serviceCause);

		throw new RuntimeException("Provider not available.");

	}

	/**
	 * Load with static provider.
	 */
	protected T providerInstance(final Class<? extends Producer<T>> providerType)
			throws Exception {
		final Producer<T> producer = providerType.newInstance();
		return producer.produce();
	}

	/**
	 * Static provider type, must be a class.
	 */
	protected abstract Class<? extends Producer<T>> providerType();

	/**
	 * Load with {@link ServiceLoader}
	 */
	protected T serviceInstance(final Class<T> serviceType) throws Exception {
		final ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceType);
		final Iterator<T> iterator = serviceLoader.iterator();
		final List<T> list = new ArrayList<T>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		switch (list.size()) {
		case 0:
			throw new IllegalStateException("Provider not available.");
		case 1:
			return list.get(0);
		default:
			throw new IllegalStateException("Duplicate providers present.");
		}
	}

	/**
	 * Target service type, must be an interface.
	 */
	protected abstract Class<T> serviceType();

}
