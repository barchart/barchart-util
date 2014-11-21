package com.barchart.util.guice.valueconverters;

import java.net.InetAddress;
import java.net.NetworkInterface;

import com.google.inject.TypeLiteral;

public class NetworkInterfaceValueConverter extends ParameterizedValueConverter {

	public NetworkInterfaceValueConverter() {
		super(TypeLiteral.get(NetworkInterface.class), "NetworkInterface");
	}

	@Override
	protected Object convertParameter(String parameter) throws Exception {
		return NetworkInterface.getByInetAddress(InetAddress.getByName(parameter));
	}

}
