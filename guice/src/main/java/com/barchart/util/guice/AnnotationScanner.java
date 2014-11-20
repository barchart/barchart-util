package com.barchart.util.guice;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Multimap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.inject.Singleton;

@Singleton
class AnnotationScanner {

	private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);

	private final Multimap<Class<? extends Annotation>, Class<?>> annotationToClassMap;

	public AnnotationScanner() {

		try {
			final ImmutableMultimap.Builder<Class<? extends Annotation>, Class<?>> builder = ImmutableMultimap.builder();

			final ClassPath classPath = ClassPath.from(AnnotationScanner.class.getClassLoader());
			for (final ClassInfo info : classPath.getTopLevelClasses()) {

				final Class<?> clazz = loadClass(info);
				if (clazz != null) {
					addAllAnnotatedClasses(builder, clazz);
				}
			}
			annotationToClassMap = builder.build();
		} catch (final Exception e) {
			throw new ExceptionInInitializerError(e);
		}

	}

	private void addAllAnnotatedClasses(final Builder<Class<? extends Annotation>, Class<?>> builder,
			final Class<?> clazz) {
		try {
			for (final Class<? extends Annotation> annotationType : getAnnotationTypes(clazz)) {
				builder.put(annotationType, clazz);
			}
			for (final Class<?> declaredClass : clazz.getDeclaredClasses()) {
				addAllAnnotatedClasses(builder, declaredClass);
			}
		} catch (final NoClassDefFoundError err) {
			logger.debug("Could not scan class: " + clazz + " because " + err.getMessage());
		}
	}

	private List<Class<? extends Annotation>> getAnnotationTypes(final Class<?> clazz) {
		final List<Class<? extends Annotation>> list = new ArrayList<Class<? extends Annotation>>();
		for (final Annotation annotation : clazz.getAnnotations()) {
			final Class<? extends Annotation> annotationType = annotation.annotationType();
			list.add(annotationType);
		}
		return list;
	}

	private Class<?> loadClass(final ClassInfo info) {
		try {
			final Class<?> load = info.load();
			return load;
		} catch (final NoClassDefFoundError error) {
			// logger.debug("Could not load: " + error.getMessage());
			return null;
		}
	}

	public Collection<Class<?>> getClassesAnnotatedWith(final Class<? extends Annotation> annotationType) {
		return annotationToClassMap.get(annotationType);
	}

}
