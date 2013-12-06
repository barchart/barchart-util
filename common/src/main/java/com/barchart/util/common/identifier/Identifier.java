package com.barchart.util.common.identifier;

public abstract class Identifier<V extends Comparable<V>, T extends Identifier<V,T>> 
		implements Comparable<T>, Existential {
	
	private final V id;
	private final Class<T> clazz;
	
	public Identifier(final V id, final Class<T> clazz) {
		
		if(id == null) {
			throw new IllegalArgumentException("ID cannot be null");
		}
		
		if(clazz == null) {
			throw new IllegalArgumentException("Class cannot be null");
		}
		
		this.id = id;
		this.clazz = clazz;
	}

	public V id() {
		return id;
	}
		
	@Override
	public int compareTo(final T o) {
		return id.compareTo(o.id());
	}

	@Override
	public boolean isNull() {
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(final Object o) {
		
		if(!(clazz.isInstance(o))) {
			return false;
		}
		
		return id.equals(clazz.cast(o).id());
	}
	
	@Override
	public String toString() {
		return id.toString();
	}
	
}
