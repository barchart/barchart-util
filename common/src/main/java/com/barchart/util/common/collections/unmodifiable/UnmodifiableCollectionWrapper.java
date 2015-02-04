package com.barchart.util.common.collections.unmodifiable;

import java.util.Collection;
import java.util.Iterator;

public class UnmodifiableCollectionWrapper<V, W extends V> implements Collection<V> {
	
	private final Collection<W> col;
	
	public UnmodifiableCollectionWrapper(final Collection<W> col) {
		this.col = col;
	}

	@Override
	public int size() {
		return col.size();
	}

	@Override
	public boolean isEmpty() {
		return col.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return col.isEmpty();
	}

	@Override
	public Iterator<V> iterator() {
		
		return new Iterator<V>() {
			
			private final Iterator<W> iter = col.iterator();

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
		return col.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return col.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return col.containsAll(c);
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
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

}
