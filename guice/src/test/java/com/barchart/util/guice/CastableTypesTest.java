package com.barchart.util.guice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

public class CastableTypesTest {

	private Set<TypeLiteral<?>> set;

	@Test
	public void testNull() {
		init(null);
	}

	@Test
	public void testObject() {
		init(Object.class);
		check(Object.class);
	}

	@Test
	public void testInteger() {
		init(Integer.class);
		check(Integer.class);
		check(Number.class);
		check(Object.class);
		check(Serializable.class);
		checkGeneric(Comparable.class, Integer.class);
	}

	@Test
	public void testString() {
		init(String.class);
		check(String.class);
		check(Object.class);
		check(CharSequence.class);
		checkGeneric(Comparable.class, String.class);
		check(Serializable.class);
	}

	private void init(Class<?> clazz) {
		set = new HashSet<TypeLiteral<?>>(CastableTypes.of(clazz));
	}

	@After
	public void teardown() {
		assertTrue("Set not empty:" + set.toString(), set.isEmpty());
	}

	private void check(Class<?> clazz) {
		assertTrue(set.remove(TypeLiteral.get(clazz)));
	}

	private void checkGeneric(Class<?> rawClass, Class<?>... genericTypes) {
		assertTrue(set.remove(TypeLiteral.get(Types.newParameterizedType(rawClass, genericTypes))));
	}

}
