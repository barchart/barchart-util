package com.barchart.util.common.identifier;

public interface Identifiable<V extends Comparable<V>, T extends Identifier<V,T>> {

	Identifier<V,T> id();
	
}
