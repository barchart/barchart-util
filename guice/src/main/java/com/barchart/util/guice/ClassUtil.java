package com.barchart.util.guice;

public class ClassUtil {

	/**
	 * Load a class by first checking the thread's class loader and then the caller's class loader.
	 * 
	 * @param className name of class to be loaded
	 * @return the class found
	 * @throws ClassNotFoundException when the class cannot be found
	 */
	public static Class<?> forName(final String className) throws ClassNotFoundException {
		return forName(className, null);
	}

	/**
	 * Load a class by directly specifying a class loader.
	 * 
	 * @param className name of class to be loaded
	 * @param classLoader the class loader to be used - if null the thread's class loader will be used first
	 * @return the class found
	 * @throws ClassNotFoundException when the class cannot be found
	 */
	public static Class<?> forName(final String className, ClassLoader classLoader) throws ClassNotFoundException {
		if (classLoader == null)
			try {
				// Check the thread's class loader
				classLoader = Thread.currentThread().getContextClassLoader();
				if (classLoader != null) {
					return Class.forName(className, true, classLoader);
				}
			} catch (final ClassNotFoundException e) {
				// not found, use the class' loader
				classLoader = null;
			}
		if (classLoader != null) {
			return Class.forName(className, true, classLoader);
		}
		return Class.forName(className);
	}

}
