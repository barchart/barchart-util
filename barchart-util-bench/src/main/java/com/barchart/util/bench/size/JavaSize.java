/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.bench.size;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Deque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * FIXME 32 bit only
 * 
 * TODO support both 32 and 64 and OOPS
 * 
 * http://wikis.sun.com/display/HotSpotInternals/CompressedOops
 * 
 */
public final class JavaSize {

	private JavaSize() {
	}

	// 32 bit java only
	public static final int REFERENCE_SIZE = 4;

	// 32 bit java only
	public static final int OBJECT_SIZE = 8;

	// 32 bit java only
	public static final int OBJECT_WORD = 8;

	public static final int of(final boolean b) {
		return 1;
	}

	public static final int of(final byte b) {
		return 1;
	}

	public static final int of(final char c) {
		return 2;
	}

	public static final int of(final short s) {
		return 2;
	}

	public static final int of(final int i) {
		return 4;
	}

	public static final int of(final long l) {
		return 8;
	}

	public static final int of(final float f) {
		return 4;
	}

	public static final int of(final double d) {
		return 8;
	}

	private static final int sizeNonArray(final Class<?> klaz,
			final Object object) {

		final Deque<Object> stack = STACK.get();

		stack.push(object);

		int size = 0;

		final Class<?> superKlaz = klaz.getSuperclass();

		if (superKlaz == null) {
			size += OBJECT_SIZE;
			// log.debug("root1={} size={}", "Object", size);
		} else {
			size += sizeNonArray(superKlaz, object);
			// log.debug("root2={} size={}", superKlaz.getName(), size);
		}

		int objFieldsSize = 0;

		// no inherited
		final Field fieldArray[] = klaz.getDeclaredFields();

		for (final Field field : fieldArray) {

			final int modifiers = field.getModifiers();

			if ((modifiers & Modifier.STATIC) != 0) {
				continue;
			}

			final Class<?> type = field.getType();

			final int fieldSize = sizePrimitive(type);

			// will count references
			objFieldsSize += fieldSize;
			// log.debug("field={} size={}", field.getName(), fieldSize);

			if (type.isPrimitive()) {
				continue;
			}

			field.setAccessible(true);

			Object value = null;
			try {
				value = field.get(object);
			} catch (final Exception e) {
				e.printStackTrace();
			}

			if (value == null) {
				continue;
			}

			if (value == object) {
				// ignore self references
				continue;
			}

			if (stack.contains(value)) {
				// ignore self references
				continue;
			}

			if (type.isArray()) {
				size += sizeArray(type, value);
			} else {
				size += sizeNonArray(type, value);
			}

		}

		// reflect word alignment
		final int wordPart = objFieldsSize % OBJECT_WORD;
		if (wordPart != 0) {
			objFieldsSize -= wordPart;
			objFieldsSize += OBJECT_WORD;
		}

		size += objFieldsSize;

		stack.pop();

		return size;

	}

	private static final int sizePrimitive(final Class<?> klaz) {
		if (klaz == Boolean.TYPE)
			return 1;
		else if (klaz == Byte.TYPE)
			return 1;
		else if (klaz == Character.TYPE)
			return 2;
		else if (klaz == Short.TYPE)
			return 2;
		else if (klaz == Integer.TYPE)
			return 4;
		else if (klaz == Long.TYPE)
			return 8;
		else if (klaz == Float.TYPE)
			return 4;
		else if (klaz == Double.TYPE)
			return 8;
		else if (klaz == Void.TYPE)
			return 0;
		else
			return REFERENCE_SIZE;
	}

	private static final int sizeArray(final Class<?> klaz, final Object object) {

		final Deque<Object> stack = STACK.get();

		stack.push(object);

		final Class<?> type = klaz.getComponentType();

		final int length = Array.getLength(object);

		int size = OBJECT_SIZE;

		if (type.isPrimitive()) {

			size += length * sizePrimitive(type);

		} else {

			for (int i = 0; i < length; i++) {

				size += REFERENCE_SIZE;

				final Object value = Array.get(object, i);

				if (value == null) {
					continue;
				}

				if (value == object) {
					// ignore self references
					continue;
				}

				if (stack.contains(value)) {
					// ignore self references
					continue;
				}

				final Class<?> itemKlaz = value.getClass();

				if (itemKlaz.isArray()) {
					size += sizeArray(itemKlaz, value);
				} else {
					size += sizeNonArray(itemKlaz, value);
				}

			}

		}

		stack.pop();

		return size;

	}

	private static final ThreadLocal<Deque<Object>> STACK = new ThreadLocal<Deque<Object>>() {
		@Override
		protected Deque<Object> initialValue() {
			return new LinkedBlockingDeque<Object>();
		}
	};

	public static final int of(final Object object) {

		if (object == null) {
			return 0;
		}

		final Deque<Object> stack = STACK.get();

		assert stack.isEmpty();

		final Class<?> klaz = object.getClass();

		final int size;

		if (klaz.isArray()) {
			size = sizeArray(klaz, object);
		} else {
			size = sizeNonArray(klaz, object);
		}

		assert stack.isEmpty();

		return size;

	}

}
