package com.barchart.util.common.status;

public class BaseStatistics implements Statistics {

	private final String name;
	private final String value;

	public BaseStatistics(String name_, String value_) {
		name = name_;
		value = value_;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String value() {
		return value;
	}
}
