package com.barchart.util.value.impl;

import java.util.ArrayList;
import java.util.Collections;

import com.barchart.util.value.api.TimeInterval;
import com.barchart.util.value.api.Schedule;

public class BaseSchedule extends ArrayList<TimeInterval> implements Schedule {

	private static final long serialVersionUID = 6395361163935322895L;

	public BaseSchedule() {
		super();
	}
	
	public BaseSchedule(final TimeInterval[] intervals) {
		super();
		Collections.addAll(this, intervals);
	}

	@Override
	public boolean isNull() {
		return this == Schedule.NULL;
	}
	
}
