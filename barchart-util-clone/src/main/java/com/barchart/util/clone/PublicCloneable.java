package com.barchart.util.clone;

public interface PublicCloneable<T> extends Cloneable {

	/**
	 * returns complete, independent, new instance via deep copy
	 */
	T clone();

}
