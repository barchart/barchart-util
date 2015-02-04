package com.barchart.util.common.collections.unmodifiable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class UnmodifiableEntrySetWrapper<K, V, W extends V> implements Set<Entry<K, V>> {
	
	private final Set<Entry<K, W>> set;

	public UnmodifiableEntrySetWrapper(final Set<Entry<K, W>> set) {
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
	public Iterator<Entry<K,V>> iterator() {
		
		return new Iterator<Entry<K, V>>() {
			
			private final Iterator<Entry<K, W>> iter = set.iterator();

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public Entry<K, V> next() {
				return new Entry<K, V>() {
					
					private final Entry<K, W> entry = iter.next();

					@Override
					public K getKey() {
						return entry.getKey();
					}

					@Override
					public V getValue() {
						return entry.getValue();
					}

					@Override
					public V setValue(V value) {
						throw new UnsupportedOperationException();
					}
					
				};
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
	public boolean add(Entry<K, V> e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends Entry<K, V>> c) {
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
