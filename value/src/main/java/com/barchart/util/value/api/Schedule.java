package com.barchart.util.value.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public interface Schedule extends List<TimeInterval>, Existential {
	
	@Override
	boolean isNull();

	Schedule NULL = new Schedule() {

		@Override
		public int size() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public boolean contains(Object o) {
			return false;
		}

		@Override
		public Iterator<TimeInterval> iterator() {
			return null;
		}

		@Override
		public Object[] toArray() {
			return null;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			return null;
		}

		@Override
		public boolean add(TimeInterval e) {
			return false;
		}

		@Override
		public boolean remove(Object o) {
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends TimeInterval> c) {
			return false;
		}

		@Override
		public boolean addAll(int index, Collection<? extends TimeInterval> c) {
			return false;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			return false;
		}

		@Override
		public void clear() {
			
		}

		@Override
		public TimeInterval get(int index) {
			return TimeInterval.NULL;
		}

		@Override
		public TimeInterval set(int index, TimeInterval element) {
			return TimeInterval.NULL;
		}

		@Override
		public void add(int index, TimeInterval element) {
			
		}

		@Override
		public TimeInterval remove(int index) {
			return TimeInterval.NULL;
		}

		@Override
		public int indexOf(Object o) {
			return 0;
		}

		@Override
		public int lastIndexOf(Object o) {
			return 0;
		}

		@Override
		public ListIterator<TimeInterval> listIterator() {
			return null;
		}

		@Override
		public ListIterator<TimeInterval> listIterator(int index) {
			return null;
		}

		@Override
		public List<TimeInterval> subList(int fromIndex, int toIndex) {
			return null;
		}

		@Override
		public boolean isNull() {
			return true;
		}
		
	};
	
}
