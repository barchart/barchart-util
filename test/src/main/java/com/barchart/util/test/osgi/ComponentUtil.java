/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.test.osgi;

import java.lang.reflect.Method;

import org.osgi.service.component.ComponentContext;

import com.barchart.osgi.component.base.BaseComponent;

public class ComponentUtil {

	public static <T extends BaseComponent> T activate(final T component) {
		return activate(component, new MockComponentContext());
	}

	public static <T extends BaseComponent> T activate(final T component,
			final String config) {
		return activate(component, new MockComponentContext(config));
	}

	public static <T extends BaseComponent> T activate(final T component,
			final ComponentContext context) {
		try {
			final Method m =
					BaseComponent.class.getDeclaredMethod("componentActivate",
							ComponentContext.class);
			m.setAccessible(true);
			m.invoke(component, context);
			return component;
		} catch (final Exception e) {
			throw new UnsupportedOperationException(
					"Could not activate component", e);
		}
	}

	public static <T extends BaseComponent> T deactivate(final T component) {
		return deactivate(component, new MockComponentContext());
	}

	public static <T extends BaseComponent> T deactivate(final T component,
			final ComponentContext context) {
		try {
			final Method m =
					BaseComponent.class.getDeclaredMethod(
							"componentDeactivate", ComponentContext.class);
			m.setAccessible(true);
			m.invoke(component, context);
			return component;
		} catch (final Exception e) {
			throw new UnsupportedOperationException(
					"Could not activate component", e);
		}
	}

	public static <T extends Object, I, P extends I> T bind(final T component,
			final P param, final Class<I> type) {

		try {
			final Method m = findMethod(component, "bind", param, type);
			m.setAccessible(true);
			m.invoke(component, param);
		} catch (final Exception e) {
			throw new UnsupportedOperationException("Could not bind component",
					e);
		}

		return component;

	}

	public static <I, P extends I> Method findMethod(final Object obj,
			final String name, final P param, final Class<I> type)
			throws NoSuchMethodException {

		Class<?> c = obj.getClass();

		while (c != null && c != Object.class) {
			try {
				return c.getDeclaredMethod(name, type);
			} catch (final Exception e) {
			}
			c = c.getSuperclass();
		}

		throw new NoSuchMethodException();

	}

}
