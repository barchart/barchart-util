package com.barchart.util.value.api;

import java.util.List;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface Schedule extends List<TimeInterval>, Copyable<Schedule> {

}
