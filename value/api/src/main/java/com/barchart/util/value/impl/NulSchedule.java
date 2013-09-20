package com.barchart.util.value.impl;

import com.barchart.util.value.api.Schedule;

public class NulSchedule extends BaseSchedule {

	private static final long serialVersionUID = 3901426132463142285L;
	
	@Override
	public Schedule copy() {
		return this;
	}

}
