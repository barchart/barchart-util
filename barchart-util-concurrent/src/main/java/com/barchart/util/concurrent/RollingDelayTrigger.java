package com.barchart.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author jeremy
 * 
 */
public class RollingDelayTrigger {

	private static final Logger log = LoggerFactory
			.getLogger(RollingDelayTrigger.class);

	private final Lock lock = new ReentrantLock();
	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor();
	private ScheduledFuture<?> scheduledRestart;

	private final Runnable task;
	private final long delay;
	private final Runnable runner;

	public RollingDelayTrigger(final Runnable task_, final long delay_) {

		task = task_;
		delay = delay_;
		runner = new TaskRunner();

	}

	/**
	 * Trigger a delayed task execution. If a task execution is already pending,
	 * its delay will be reset.
	 */
	public void trigger() {

		lock.lock();
		try {

			cancel();
			scheduledRestart = executor.schedule(runner, delay,
					TimeUnit.MILLISECONDS);

		} finally {
			lock.unlock();
		}

	}

	/**
	 * Cancel any pending delayed tasks until the next call to trigger().
	 */
	public void cancel() {

		lock.lock();
		try {

			if (scheduledRestart != null) {
				scheduledRestart.cancel(false);
				scheduledRestart = null;
			}

		} finally {
			lock.unlock();
		}

	}

	private class TaskRunner implements Runnable {

		@Override
		public void run() {

			try {

				lock.lock();
				try {
					scheduledRestart = null;
				} finally {
					lock.unlock();
				}

				task.run();

			} catch (final Exception e) {
				log.warn("Exception running delay trigger task", e);
			}

		}

	}

}
