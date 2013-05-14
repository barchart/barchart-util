package com.barchart.util.value.api;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface TimeInterval extends Copyable<TimeInterval> {

	Time start();

	Time stop();

}
