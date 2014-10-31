package com.barchart.util.guice;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.junit.Test;

import com.barchart.util.guice.component.Component;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvidedBy;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.name.Names;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

public class DynamicTest {

	public static class Target {

		@Inject
		@Named("targetvalue")
		private String value;

		@Inject
		@Named("dep1")
		private Dependency dependency;

		@Override
		public String toString() {
			return "Target [value=" + value + ", dependency=" + dependency + "]";
		}

	}

	@Component(value = "dependency")
	public static class Dependency {

		private final String value;

		public Dependency(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return "Dependency [value=" + value + "]";
		}

	}

	public static final class ProviderImpl {

	}

	@Test
	public void dynamicBinding() {
		AbstractModule module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named(("targetvalue"))).toInstance("hello");

				Dependency dependency = new Dependency("World");
				Class<?> clazz = Dependency.class;

				@SuppressWarnings("unchecked")
				AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) bind(clazz);
				bindingBuilder.annotatedWith(Names.named("dep1")).toInstance(dependency);

			}
		};

		List<Element> elements = Elements.getElements(module);
		for (Element e : elements) {
			System.out.println("Element: " + e);
		}

		Injector injector = Guice.createInjector(module);

		Target target = injector.getInstance(Target.class);
		System.out.println("Target: " + target);

	}

}
