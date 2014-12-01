package com.barchart.util.guice.valueconverters;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;

public class NetworkInterfaceValueConverter extends ParameterizedValueConverter {

	private static final Logger logger = LoggerFactory.getLogger(NetworkInterfaceValueConverter.class);

	public NetworkInterfaceValueConverter() {
		super(TypeLiteral.get(NetworkInterface.class), "NetworkInterface");
	}

	@Override
	protected Object convertParameter(String parameter) throws Exception {
		try {
			InetAddress inetAddress = InetAddress.getByName(parameter);
			return NetworkInterface.getByInetAddress(inetAddress);
		} catch (UnknownHostException e) {
			// Not ideal, but if we throw an exception here, than we can't do a verifying build
			// on machines without the interfaces configured
			NetworkInterface loopback = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());
			logger.error("Could not resolve network interface.  Using loopback instead: " + loopback, e);
			return loopback;
		}
	}

}
