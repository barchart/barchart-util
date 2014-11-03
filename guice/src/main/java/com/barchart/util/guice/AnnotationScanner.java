package com.barchart.util.guice;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.inject.Singleton;

@Singleton
class AnnotationScanner {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);

	private static final Multimap<Class<? extends Annotation>, Class<?>> annotationToClassMap;

	static {
		try {
			ImmutableMultimap.Builder<Class<? extends Annotation>, Class<?>> builder = ImmutableMultimap.builder();

			ClassPath classPath = ClassPath.from(AnnotationScanner.class.getClassLoader());
			for (ClassInfo info : classPath.getTopLevelClasses()) {
				Class<?> clazz = loadClass(info);
				if (clazz != null) {
					for (Class<? extends Annotation> annotationType : getAnnotationTypes(clazz)) {
						builder.put(annotationType, clazz);
					}

					for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
						for (Class<? extends Annotation> annotationType : getAnnotationTypes(declaredClass)) {
							builder.put(annotationType, declaredClass);
						}
					}
				}
			}
			annotationToClassMap = builder.build();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public AnnotationScanner() {
	}

	private static List<Class<? extends Annotation>> getAnnotationTypes(Class<?> clazz) {
		List<Class<? extends Annotation>> list = new ArrayList<Class<? extends Annotation>>();
		for (Annotation annotation : clazz.getAnnotations()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			list.add(annotationType);
		}
		return list;
	}

	private static Class<?> loadClass(ClassInfo info) {
		try {
			Class<?> load = info.load();
			return load;
		} catch (NoClassDefFoundError error) {
			// logger.debug("Could not load: " + error.getMessage());
			return null;
		}
	}

	public Collection<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> annotationType) {
		return annotationToClassMap.get(annotationType);
	}
}
