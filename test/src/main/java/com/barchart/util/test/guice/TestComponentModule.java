package com.barchart.util.test.guice;

import java.util.ArrayList;
import java.util.List;

import com.barchart.util.guice.ConfigBinder;
import com.barchart.util.guice.ValueConverterTool;
import com.google.inject.AbstractModule;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;

/**
 * Module that provides a component for injection using the specified config for use in unit testing.
 */
public class TestComponentModule<T> extends AbstractModule {

	private final Class<T> type;
	private final List<TypeLiteral<? super T>> interfaces;
	private String config;

	protected TestComponentModule(final Class<T> type_) {
		type = type_;
		interfaces = new ArrayList<TypeLiteral<? super T>>();
	}

	public static <T> TestComponentModule<T> forType(final Class<T> type_) {
		return new TestComponentModule<T>(type_);
	}

	public TestComponentModule<T> provide(final Class<? super T>... classes_) {

		for (final Class<? super T> c : classes_) {
			interfaces.add(TypeLiteral.get(c));
		}

		return this;

	}

	public TestComponentModule<T> provide(final TypeLiteral<? super T>... types_) {

		for (final TypeLiteral<? super T> t : types_) {
			interfaces.add(t);
		}

		return this;

	}

	public TestComponentModule<T> config(final String config_) {
		config = config_;
		return this;
	}

	@Override
	protected void configure() {

		install(new TestComponentConfigModule());

		if (interfaces.size() > 0) {
			for (final TypeLiteral<?> cast : interfaces) {
				@SuppressWarnings("unchecked")
				final Multibinder<Object> multibinder = (Multibinder<Object>) Multibinder.newSetBinder(binder(), cast);
				multibinder.addBinding().to(type);
			}
		}

	}

	private final class TestComponentConfigModule extends PrivateModule {

		private final Config cfg;

		public TestComponentConfigModule() {
			cfg = ConfigFactory.parseString(config,
					ConfigParseOptions.defaults().setOriginDescription("/" + type.getSimpleName() + ".component"));
		}

		@Override
		protected void configure() {

			final ConfigBinder binder = new ConfigBinder(ValueConverterTool.defaultValueConverterTool());

			binder.applyBindings(binder(), cfg, "#");

			bind(type);
			expose(type);

			if (interfaces.size() > 0) {
				for (final TypeLiteral<? super T> t : interfaces) {
					bind(t).to(type);
					expose(t);
				}
			}

		}

	}

}
