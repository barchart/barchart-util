/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.test.osgi;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.ComponentInstance;

import com.barchart.osgi.component.base.HoconComponent;

public class MockComponentContext implements ComponentContext {

	Dictionary<Object, Object> properties = new Hashtable<Object, Object>();

	public MockComponentContext() {
	}

	public MockComponentContext(final String config) {
		properties.put(HoconComponent.OSGI_CONF, config);
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
