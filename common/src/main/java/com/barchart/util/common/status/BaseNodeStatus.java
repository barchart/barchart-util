package com.barchart.util.common.status;

public abstract class BaseNodeStatus extends BaseStatus implements NodeStatus {

	private final String id;
	private final String address;

	public BaseNodeStatus(final String id_, final String name_, final String address_) {
		super(name_);
		id = id_;
		address = address_;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String address() {
		return address;
	}

}
