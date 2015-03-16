package com.barchart.util.common.status;

import java.util.Deque;
import java.util.LinkedList;

public abstract class BaseStatus {

	private StatusType status = StatusType.OK;
	private String name;
	private String message;
	private long timestamp;
	private int errorCount;
	private Deque<Long> flaps = new LinkedList<Long>();

	public BaseStatus(final String name_) {
		name = name_;
	}

	public StatusType status() {
		return status;
	}

	protected void status(final StatusType status_) {
		if (status_ != status) {
			timestamp = System.currentTimeMillis();
			flaps.add(timestamp);
			final int remove = flaps.size() - 101;
			for (int i = 0; i < remove; i++) {
				flaps.pollFirst();
			}
			if (status == StatusType.OK) {
				errorCount = 0;
			}
		}
		if (status_ != StatusType.OK) {
			errorCount++;
		}
		status = status_;
	}

	protected void status(final StatusType status_, final String message_) {
		status(status_);
		message = message_;
	}

	public String name() {
		return name;
	}

	public String message() {
		return message;
	}

	public long timestamp() {
		return timestamp;
	}

	public int errorCount() {
		return errorCount;
	}

	public int flaps(final int minutes) {

		final long from = System.currentTimeMillis() - (60 * minutes * 1000);

		int count = 0;

		for (final Long ts : flaps) {
			if (ts >= from) {
				count++;
			}
		}

		return count;

	}

}
