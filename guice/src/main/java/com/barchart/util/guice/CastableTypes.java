package com.barchart.util.guice;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.inject.TypeLiteral;

/**
 * 
 * Iterable over all types a class can safely cast to, including itself. Include
 * this class, all supertypes and implemented interfaces. If this base class is
 * null, then return an emptylist
 *
 */
final class CastableTypes extends AbstractCollection<TypeLiteral<?>> {

	private final Collection<TypeLiteral<?>> collection;

	CastableTypes(Collection<TypeLiteral<?>> collection) {
		this.collection = collection;
	}

	@Override
	public int size() {
		return collection.size();
	}

	@Override
	public Iterator<TypeLiteral<?>> iterator() {
		return collection.iterator();
	}

	@Override
	public String toString() {
		return Iterables.toString(collection);
	}

	public static CastableTypes of(Class<?> baseclass) {
		List<TypeLiteral<?>> list = new ArrayList<TypeLiteral<?>>();
		if (baseclass != null) {
			for (TypeLiteral<?> type : getInclusiveSuperclasses(TypeLiteral.get(baseclass))) {
				list.add(type);
				list.addAll(getInterfaces(type));
			}
		}
		return new CastableTypes(list);
	}

	private static Collection<TypeLiteral<?>> getInterfaces(TypeLiteral<?> type) {
		List<TypeLiteral<?>> list = new ArrayList<TypeLiteral<?>>();
		Class<?> rawType = type.getRawType();
		for (Class<?> iface : rawType.getInterfaces()) {
			list.add(type.getSupertype(iface));
		}
		return list;
	}

	// Return a collection of this class (if not null), and all superclasses
	private static Collection<TypeLiteral<?>> getInclusiveSuperclasses(TypeLiteral<?> type) {
		List<TypeLiteral<?>> list = new ArrayList<TypeLiteral<?>>();
		while (type != null) {
			list.add(type);
			Class<?> rawType = type.getRawType();
			Class<?> rawSuperclass = rawType.getSuperclass();
			type = rawSuperclass == null ? null : type.getSupertype(rawType.getSuperclass());
		}
		return list;
	}

}
