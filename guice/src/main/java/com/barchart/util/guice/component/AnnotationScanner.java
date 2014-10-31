package com.barchart.util.guice.component;

import java.lang.annotation.Annotation;
import java.util.Collection;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.inject.Singleton;

@Singleton
class AnnotationScanner {

	private static final Logger logger = LoggerFactory.getLogger(AnnotationScanner.class);

	private Multimap<Class<? extends Annotation>, Class<?>> annotationToClassMap;

	public AnnotationScanner() {
		annotationToClassMap = HashMultimap.create();
		logger.info("Start scanning");
		scan();
		logger.info("Stop scanning");
	}

	private void scan() {
		
		try {
			ClassPath classPath = ClassPath.from(getClass().getClassLoader());
			for (ClassInfo info : classPath.getTopLevelClasses()) {
				Class<?> clazz = loadClass(info);
				if (clazz != null) {
					checkForAnnotations(clazz);
					for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
						checkForAnnotations(declaredClass);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void checkForAnnotations(Class<?> clazz) {
		for (Annotation annotation : clazz.getAnnotations()) {
			Class<? extends Annotation> annotationType = annotation.annotationType();
			annotationToClassMap.put(annotationType, clazz);
		}
	}

	private Class<?> loadClass(ClassInfo info) {
		try {
			Class<?> load = info.load();
			return load;
		} catch (NoClassDefFoundError error) {
			logger.debug("Could not load: " + error.getMessage());
			return null;
		}
	}

	public Collection<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> annotationType) {
		return annotationToClassMap.get(annotationType);
	}
}
