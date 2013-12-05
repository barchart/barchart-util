package com.barchart.util.common.flow.design;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class ExecutorSynchronous implements Executor {

	final ArrayList<Runnable> queue = new ArrayList<Runnable>();

	volatile boolean isRunning = false;

	@Override
	public void execute(final Runnable task) {

		queue.add(task);

		if (isRunning) {
			return;
		}

		while (!queue.isEmpty()) {
			final Runnable nextTask = queue.remove(queue.size() - 1);
			isRunning = true;
			nextTask.run();
			isRunning = false;
		}

	}

}
