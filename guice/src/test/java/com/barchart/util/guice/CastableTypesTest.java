package com.barchart.util.guice;

import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
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

	@Test
	public void testInterfaceAncestry() {
		init(TestChild.class);
		check(TestChild.class);
		check(ChildInterface.class);
		check(ParentInterface.class);
		check(Object.class);
	}

	private void init(final Class<?> clazz) {
		set = new HashSet<TypeLiteral<?>>(CastableTypes.of(clazz));
	}

	@After
	public void teardown() {
		assertTrue("Set not empty:" + set.toString(), set.isEmpty());
	}

	private void check(final Class<?> clazz) {
		assertTrue("Does not match " + clazz, set.remove(TypeLiteral.get(clazz)));
	}

	private void checkGeneric(final Class<?> rawClass, final Class<?>... genericTypes) {
		assertTrue(set.remove(TypeLiteral.get(Types.newParameterizedType(rawClass, genericTypes))));
	}

	public static interface ParentInterface {}

	public static class TestParent implements ParentInterface {}

	public static interface ChildInterface extends ParentInterface {}

	public static class TestChild implements ChildInterface {}

}
