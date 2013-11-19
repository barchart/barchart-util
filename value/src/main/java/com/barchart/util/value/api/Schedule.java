package com.barchart.util.value.api;

import java.util.List;

import com.barchart.util.value.FactoryImpl;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Schedule extends List<TimeInterval>, Existential {
	
	/** Special schedule value @see {isNull} */
	Schedule NULL = new FactoryImpl().newSchedule(new TimeInterval[0]);
	
	@Override
	boolean isNull();

}
