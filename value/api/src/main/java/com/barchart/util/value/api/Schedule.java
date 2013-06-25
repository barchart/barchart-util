package com.barchart.util.value.api;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Schedule extends List<TimeInterval>, Copyable<Schedule> {
	
	/** Special schedule value @see {isNull} */
	Schedule NULL = FactoryLoader.load().newSchedule(new TimeInterval[0]);

}
