package com.barchart.util.common.status;

public class BaseStatistics implements Statistics {

	private final String name;
	private final String count;
	private final String min;
	private final String max;
	private final String mean;

	public BaseStatistics(String name_, String count_, String min_, String max_, String mean_) {
		name = name_;
		count = count_;
		min = min_;
		max = max_;
		mean = mean_;
	}

	public BaseStatistics(String name_, String count_) {
		name = name_;
		count = count_;
		min = "0.0";
		max = "0.0";
		mean = "0.0";
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String count() {
		return count;
	}

	@Override
	public String min() {
		return min;
	}

	@Override
	public String max() {
		return max;
	}

	@Override
	public String mean() {
		return mean;
	}

}
