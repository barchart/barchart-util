package com.barchart.util.guice;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class ComponentActivator implements Module {

	private static final Logger logger = LoggerFactory.getLogger(ComponentActivator.class);
	
	private static final AbstractMatcher<TypeLiteral<?>> MATCHER = new AbstractMatcher<TypeLiteral<?>>() {

		@Override
		public boolean matches(final TypeLiteral<?> t) {
			return activator(Activate.class, t.getRawType()) != null;
		}

	};

	@Override
	public void configure(final Binder binder) {
		binder.bindListener(MATCHER, new TypeListener() {
			@Override
			public <I> void hear(final TypeLiteral<I> type, final TypeEncounter<I> encounter) {
				encounter.register(new InjectionListener<I>() {
					@Override
					public void afterInjection(final I injectee) {
						try {
							final Method activator = activator(Activate.class, injectee.getClass());
							activator.setAccessible(true);
							activator.invoke(injectee);
						} catch (final Exception e) {
							logger.error("Could not activate " + injectee.getClass(), e);
							throw new RuntimeException(e);
						}
					}
				});
			}
		});

	}

	private static Method activator(final Class<?> annotation, final Class<?> type) {

		for (final Method m : type.getDeclaredMethods()) {

			final Activate anno = m.getAnnotation(Activate.class);

			if (anno != null) {
				return m;
			}

		}

		final Class<?> supertype = type.getSuperclass();

		if (supertype != null)
			return activator(annotation, supertype);

		return null;

	}

}
