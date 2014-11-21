package com.barchart.util.guice;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;

@Singleton
class AnnotationScanner {

	private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);

	private final Set<Class<?>> componentClasses;

	private final Set<Class<?>> configuredModuleClasses;

	public AnnotationScanner() {

		final Collection<URL> urls = ClasspathHelper.forPackage("META-INF/reflections/");

		// Load from cache if available (requires dom4j dependency in including project to avoid NoClassDefFound)
		final Reflections reflections = urls.size() > 0 ? Reflections.collect() : new Reflections("");

		this.componentClasses = ImmutableSet.copyOf(reflections.getTypesAnnotatedWith(Component.class));
		this.configuredModuleClasses = ImmutableSet.copyOf(reflections.getTypesAnnotatedWith(ConfiguredModule.class));

	}

	public Collection<Class<?>> getComponentClasses() {
		return componentClasses;
	}

	public Collection<Class<?>> getConfiguredModuleClasses() {
		return configuredModuleClasses;
	}

}
