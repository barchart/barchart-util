package com.barchart.util.common.status;

public interface ScalingMonitor {

	enum Usage {
		LOW, NORMAL, HIGH
	};

	Usage usage();

	long started();

	int periods();

}
