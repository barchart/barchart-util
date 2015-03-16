package com.barchart.util.common.status;

import java.util.Deque;
import java.util.LinkedList;

public abstract class BaseComponentStatus implements ComponentStatus {

	private Status status = Status.OK;
	private Optionality optionality = Optionality.OPTIONAL;
	private Locality locality = Locality.LOCAL;
	private String name;
	private String message;
	private long timestamp;
	private int errorCount;
	private Deque<Long> flaps = new LinkedList<Long>();

	public BaseComponentStatus(final String name_) {
		name = name_;
	}

	public BaseComponentStatus(final String name_, final Optionality optionality_, final Locality locality_) {
		name = name_;
		optionality = optionality_;
		locality = locality_;
	}

	@Override
	public Status status() {
		return status;
	}

	protected void status(final Status status_) {
		if (status_ != status) {
			timestamp = System.currentTimeMillis();
			flaps.add(timestamp);
			final int remove = flaps.size() - 101;
			for (int i = 0; i < remove; i++) {
				flaps.pollFirst();
			}
			if (status == Status.OK) {
				errorCount = 0;
			}
		}
		if (status_ != Status.OK) {
			errorCount++;
		}
		status = status_;
	}

	protected void status(final Status status_, final String message_) {
		status(status_);
		message = message_;
	}

	@Override
	public Optionality optionality() {
		return optionality;
	}

	protected void optionality(final Optionality optionality_) {
		optionality = optionality_;
	}

	@Override
	public Locality locality() {
		return locality;
	}

	protected void locality(final Locality locality_) {
		locality = locality_;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String message() {
		return message;
	}

	@Override
	public long timestamp() {
		return timestamp;
	}

	@Override
	public int errorCount() {
		return errorCount;
	}

	@Override
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
