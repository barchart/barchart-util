package com.barchart.util.value.api;

import java.util.List;

import com.barchart.util.value.impl.ValueConst;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Schedule extends List<TimeInterval>, Existential {
	
	/** Special schedule value @see {isNull} */
	Schedule NULL = ValueConst.NULL_SCHEDULE;
	
	@Override
	boolean isNull();

}
