package com.barchart.util.test.osgi;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.osgi.service.component.ComponentContext;

import com.barchart.osgi.component.base.BaseComponent;

public class ComponentInitializer<C extends BaseComponent> {

	private final C component;
	private ComponentContext context = null;

	public ComponentInitializer(final Class<C> type) {
		try {
			component = type.getConstructor().newInstance();
		} catch (final Throwable t) {
			throw new RuntimeException(t);
		}
	}

	public ComponentInitializer(final C comp) {
		component = comp;
	}

	protected Set<Class<?>> types(Class<?> type) {

		final Set<Class<?>> types = new HashSet<Class<?>>();

		while (type != null) {

			types.add(type);

			for (final Class<?> iface : type.getInterfaces())
				types.addAll(types(iface));

			type = type.getSuperclass();

		}

		return types;

	}

	protected Method method(final Object subject, final String method, final Object... params)
			throws NoSuchMethodException {

		for (final Class<?> sType : types(subject.getClass())) {

			methodloop:
			for (final Method m : sType.getDeclaredMethods()) {

				// Method name matches
				if (m.getName().equals(method)) {

					final Class<?>[] mTypes = m.getParameterTypes();

					// Parameter length mismatch, continue
					if (mTypes.length != params.length)
						continue;

					paramloop:
					for (int i = 0; i < params.length; i++) {

						for (final Class<?> pType : types(params[i].getClass())) {
							if (pType == mTypes[i])
								// Matches, check next param
								continue paramloop;
						}

						// No match, check next method
						continue methodloop;

					}

					// All matched, return method
					return m;

				}

			}

		}

		throw new NoSuchMethodException();

	}

	public <S> ComponentInitializer<C> bind(final S service) {

		try {

			final Method m = method(component, "bind", service);
			m.setAccessible(true);
			m.invoke(component, service);
			return this;

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	public <S> ComponentInitializer<C> bind(final S service, final Class<? super S> type) {
		ComponentUtil.bind(component, service, type);
		return this;
	}

	public C activate() {
		return activate(new MockComponentContext());
	}

	public C activate(final String config) {
		return activate(new MockComponentContext(config));
	}

	public C activate(final ComponentContext context_) {

		try {

			context = context_;

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

	public C deactivate() {

		if (context == null)
			throw new IllegalStateException("Component has not been activated");

		try {

			final Method m =
					BaseComponent.class.getDeclaredMethod(
							"componentDeactivate", ComponentContext.class);

			m.setAccessible(true);
			m.invoke(component, context);

			context = null;

			return component;

		} catch (final Exception e) {
			throw new UnsupportedOperationException(
					"Could not activate component", e);
		}
	}

	public C component() {
		return component;
	}

}
