package com.barchart.util.common.collections.unmodifiable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class UnmodifiableSetWrapper<V, W extends V> implements Set<V> {
	
	private final Set<W> set;
	
	public UnmodifiableSetWrapper(final Set<W> set) {
		this.set = set;
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean contains(final Object o) {
		return set.contains(o);
	}

	@Override
	public Iterator<V> iterator() {
		
		return new Iterator<V>() {
			
			private final Iterator<W> iter = set.iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public V next() {
				return iter.next();
			}

			@Override
			public void remove() {
				iter.remove();
			}
			
		};
	}

	@Override
	public Object[] toArray() {
		return set.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return set.toArray(a);
	}
	
	@Override
	public boolean containsAll(final Collection<?> c) {
		return set.containsAll(c);
	}

	@Override
	public boolean add(V e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends V> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

}
