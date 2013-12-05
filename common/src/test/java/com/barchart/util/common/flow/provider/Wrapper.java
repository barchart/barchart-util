package com.barchart.util.common.flow.provider;

public class Wrapper<T> {

	private volatile T value;

	public T value() {
		return value;
	}

	public void value(final T value) {
		this.value = value;
	}

}
