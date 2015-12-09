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
			 NetworkInterface intf = NetworkInterface.getByInetAddress(inetAddress);
			 if(intf == null) {
				 // Java 8 returns a null instead of throwing an exception
				 NetworkInterface loopback = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());
				 logger.error("Could not resolve network interface: "+ parameter + " Using loopback instead: " + loopback);
				 intf = loopback;
			 }
			 return intf;
			
		} catch (Exception e) {
			// Not ideal, but if we throw an exception here, than we can't do a verifying build
			// on machines without the interfaces configured
			NetworkInterface loopback = NetworkInterface.getByInetAddress(InetAddress.getLoopbackAddress());
			logger.error("Could not resolve network interface.  Using loopback instead: " + loopback, e);
			return loopback;
		}
	}

}
