package com.barchart.util.guice;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.google.inject.TypeLiteral;

/**
 *
 * Iterable over all types a class can safely cast to, including itself. Include
 * this class, all supertypes and implemented interfaces. If this base class is
 * null, then return an emptylist
 *
 */
public final class CastableTypes extends AbstractCollection<TypeLiteral<?>> {

	private final Collection<TypeLiteral<?>> collection;

	CastableTypes(final Collection<TypeLiteral<?>> collection) {
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

	public static CastableTypes of(final Class<?> baseclass) {
		final Set<TypeLiteral<?>> set = new HashSet<TypeLiteral<?>>();
		if (baseclass != null) {
			for (final TypeLiteral<?> type : getInclusiveSuperclasses(TypeLiteral.get(baseclass))) {
				set.add(type);
				set.addAll(getInterfaces(type));
			}
		}
		return new CastableTypes(set);
	}

	private static Collection<TypeLiteral<?>> getInterfaces(final TypeLiteral<?> type) {
		final Set<TypeLiteral<?>> set = new HashSet<TypeLiteral<?>>();
		final Class<?> rawType = type.getRawType();
		for (final Class<?> iface : rawType.getInterfaces()) {
			final TypeLiteral<?> itype = type.getSupertype(iface);
			set.add(itype);
			set.addAll(getInterfaces(itype));
		}
		return set;
	}

	// Return a collection of this class (if not null), and all superclasses
	private static Collection<TypeLiteral<?>> getInclusiveSuperclasses(TypeLiteral<?> type) {
		final Set<TypeLiteral<?>> set = new HashSet<TypeLiteral<?>>();
		while (type != null) {
			set.add(type);
			final Class<?> rawType = type.getRawType();
			final Class<?> rawSuperclass = rawType.getSuperclass();
			type = rawSuperclass == null ? null : type.getSupertype(rawType.getSuperclass());
		}
		return set;
	}

}
