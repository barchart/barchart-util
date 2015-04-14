package com.barchart.util.common.status;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class BaseComponentStatus extends BaseStatus implements ComponentStatus {

	private Optionality optionality = Optionality.OPTIONAL;
	private Locality locality = Locality.LOCAL;

	public BaseComponentStatus(final String name_) {
		super(name_);
	}

	public BaseComponentStatus(final String name_, final Optionality optionality_, final Locality locality_) {
		super(name_);
		optionality = optionality_;
		locality = locality_;
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
	public Set<NodeStatus> nodes() {
		return Collections.emptySet();
	}

	@Override
	public Map<String, Set<Statistics>> stats() {
		return Collections.emptyMap();
	}

}
