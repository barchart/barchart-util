package com.barchart.util.guice;

import java.util.Collection;
import java.util.Set;

import org.reflections.Reflections;
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
		Reflections reflections = new Reflections("");
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
