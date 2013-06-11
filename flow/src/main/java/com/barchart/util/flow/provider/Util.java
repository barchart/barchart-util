package com.barchart.util.flow.provider;

import java.lang.reflect.Method;

/**
 * Machine utilities.
 */
class Util {

	/**
	 * Verify class extends enum.
	 */
	static void assertEnumType(final Class<?> klaz) {
		if (Enum.class.isAssignableFrom(klaz)) {
			return;
		}
		throw new IllegalStateException("Type must extend Enum. " + klaz);
	}

	/**
	 * Verify enum has members.
	 */
	static void assertEnumNotEmpty(final Class<?> klaz) {
		if (enumValues(klaz).length > 0) {
			return;
		}
		throw new IllegalStateException("Enum must not be empty. " + klaz);
	}

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
