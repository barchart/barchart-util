package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Iterable over all types a class can safely cast to, including itself. Include
 * this class, all supertypes and implemented interfaces. If this base class is
 * null, then return an emptylist
 *
 */
final class CastableTypes implements Iterable<Class<?>> {

	private final Iterable<Class<?>> iterable;

	CastableTypes(Iterable<Class<?>> iterable) {
		this.iterable = iterable;
	}

	@Override
	public Iterator<Class<?>> iterator() {
		return iterable.iterator();
	}

	public static CastableTypes of(Class<?> baseclass) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> clazz : getInclusiveSuperclasses(baseclass)) {
			list.add(clazz);
			list.addAll(Arrays.asList(clazz.getInterfaces()));
		}
		return new CastableTypes(list);
	}

	// Return a collection of this class (if not null), and all superclasses
	private static Collection<Class<?>> getInclusiveSuperclasses(Class<?> clazz) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		while (clazz != null) {
			list.add(clazz);
			clazz = clazz.getSuperclass();
		}
		return list;
	}

}
