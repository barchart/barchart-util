package com.barchart.util.guice;

import java.io.Serializable;
import java.lang.annotation.Annotation;

class IndexedImpl implements Indexed, Serializable {

	private final int index;

	public IndexedImpl(int index) {
		this.index = index;
	}

	public int index() {
		return this.index;
	}

	public int hashCode() {
		// This is specified in java.lang.Annotation.
		return (127 * "index".hashCode()) ^ Integer.valueOf(index).hashCode();
	}

	public boolean equals(Object o) {
		if (!(o instanceof Indexed)) {
			return false;
		}

		Indexed other = (Indexed) o;
		return index == other.index();
	}

	public String toString() {
		return "@" + Indexed.class.getName() + "(index=" + index + ")";
	}

	public Class<? extends Annotation> annotationType() {
		return Indexed.class;
	}

	private static final long serialVersionUID = 0;
}