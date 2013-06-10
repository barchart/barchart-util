package com.barchart.util.flow.provider;

import java.lang.reflect.Method;

/**
 * Machine utilities.
 */
class Util {

	/**
	 * Extract value array form {@link Enum} class.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static <T> T[] enumValues(final Class<?> baseClass) {
		try {
			final Class<Enum> enumClass = (Class<Enum>) baseClass;
			final Method method = enumClass.getDeclaredMethod("values");
			method.setAccessible(true);
			final T[] array = (T[]) method.invoke(null);
			return array;
		} catch (final Throwable e) {
			throw new IllegalStateException("Enum failure.", e);
		}
	}

	private Util() {
	}

}
