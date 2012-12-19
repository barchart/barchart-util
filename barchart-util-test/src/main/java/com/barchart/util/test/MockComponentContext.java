/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.test;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentInstance;

import com.barchart.conf.util.BaseComp;

public class MockComponentContext implements ComponentContext {

	Dictionary<Object, Object> properties = new Hashtable<Object, Object>();

	public MockComponentContext() {
	}

	public MockComponentContext(final String config) {
		properties.put(BaseComp.PROP_CONFIG, config);
		// final Config c = ConfigFactory.parseString(config);
		// final Map<String, String> props = Util.wrap(c);
		// for (final Map.Entry<String, String> entry : props.entrySet()) {
		// properties.put(entry.getKey(), entry.getValue());
		// }
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Dictionary getProperties() {
		return properties;
	}

	@Override
	public Object locateService(final String name) {
		return null;
	}

	@Override
	public Object locateService(final String name,
			final ServiceReference reference) {
		return null;
	}

	@Override
	public Object[] locateServices(final String name) {
		return null;
	}

	@Override
	public BundleContext getBundleContext() {
		return null;
	}

	@Override
	public Bundle getUsingBundle() {
		return null;
	}

	@Override
	public ComponentInstance getComponentInstance() {
		return null;
	}

	@Override
	public void enableComponent(final String name) {
	}

	@Override
	public void disableComponent(final String name) {
	}

	@Override
	public ServiceReference getServiceReference() {
		return null;
	}

}
