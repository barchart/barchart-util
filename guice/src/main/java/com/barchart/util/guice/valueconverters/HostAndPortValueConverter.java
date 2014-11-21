package com.barchart.util.guice.valueconverters;

import com.google.common.net.HostAndPort;
import com.google.inject.TypeLiteral;

public class HostAndPortValueConverter extends ParameterizedValueConverter {

	public HostAndPortValueConverter() {
		super(TypeLiteral.get(HostAndPort.class), "HostAndPort");
	}

	@Override
	protected Object convertParameter(String parameter) throws Exception {
		return HostAndPort.fromString(parameter);
	}

}
