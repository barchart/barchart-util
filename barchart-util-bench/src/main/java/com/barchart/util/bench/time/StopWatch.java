/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.bench.time;

import java.util.concurrent.atomic.AtomicBoolean;

public class StopWatch {

	private final AtomicBoolean isRunning = new AtomicBoolean(false);

	private volatile long start;
	private volatile long stop;

	public void clear() {
		final long now = System.nanoTime();
		start = now;
		stop = now;
		isRunning.set(false);
	}

	public boolean isRunning() {
		return isRunning.get();
	}

	/**
	 * @param duration
	 *            - nano
	 */
	public boolean hasExceeded(final long duration) {
		return System.nanoTime() - start > duration;
	}

	public void start() {
		if (isRunning.compareAndSet(false, true)) {
			start = System.nanoTime();
		}
	}

	public void startNow() {
		start = System.nanoTime();
	}

	public void stop() {
		if (isRunning.compareAndSet(true, false)) {
			stop = System.nanoTime();
		}
	}

	public void stopNow() {
		stop = System.nanoTime();
	}

	/** nano */
	public long getDiff() {
		return stop - start;
	}

	/** nano */
	public long getStart() {
		return start;
	}

	/** nano */
	public long getStop() {
		return stop;
	}

	/** nano */
	public long getDiffNow() {
		return System.nanoTime() - start;
	}

	/** per nano */
	private final static double ONE_SECOND = 1000 * 1000 * 1000;
	private final static double ONE_MILLI = 1000 * 1000;
	private final static double ONE_MICRO = 1000;

	public String toStringPretty() {

		final double nanoDiff = stop - start;

		final int seconds = (int) (nanoDiff / ONE_SECOND);

		final int millis = (int) ((nanoDiff - seconds * ONE_SECOND) / ONE_MILLI);

		final int micros = (int) ((nanoDiff - seconds * ONE_SECOND - millis
				* ONE_MILLI) / ONE_MICRO);

		final int nanos = (int) (nanoDiff - seconds * ONE_SECOND - millis
				* ONE_MILLI - micros * ONE_MICRO);

		return String.format("%d s %d ms %d us %d ns ", seconds, millis,
				micros, nanos);

	}

	@Override
	public String toString() {
		return "nanoTime: " + Long.toString(getDiff());
	}

}
