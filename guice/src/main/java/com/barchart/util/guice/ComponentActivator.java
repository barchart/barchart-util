package com.barchart.util.guice;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class ComponentActivator implements Module {

	private static final AbstractMatcher<TypeLiteral<?>> MATCHER = new AbstractMatcher<TypeLiteral<?>>() {
		@Override
		public boolean matches(TypeLiteral<?> t) {
			return Activatable.class.isAssignableFrom(t.getRawType());
		}
	};

	@Override
	public void configure(Binder binder) {
		binder.bindListener(MATCHER, new TypeListener() {
			@Override
			public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
				encounter.register(new InjectionListener<I>() {
					@Override
					public void afterInjection(I injectee) {
						try {
							Activatable activatable = (Activatable) injectee;
							activatable.activate();
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
				});
			}
		});

	}

}
