package com.barchart.util.common.flow.design;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorAsynchronous implements Executor {

	private final Executor executor = Executors.newSingleThreadExecutor();

	@Override
	public void execute(final Runnable task) {
		executor.execute(task);
	}

}
