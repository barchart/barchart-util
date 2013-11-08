package com.barchart.util.value.api;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Schedule extends List<TimeInterval>, Existential {
	
	/** Special schedule value @see {isNull} */
	Schedule NULL = FactoryLoader.load().newSchedule(new TimeInterval[0]);
	
	@Override
	boolean isNull();

}
