package com.barchart.util.test.networking;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 * Helper with some common http related issues
 *
 * @author Maurycy
 *
 */
public class SocketHelper {

	/**
	 * Checks to see if a specific local port is available.
	 *
	 * @param port
	 *            the port to check for availability
	 */
	public static boolean localPortAvailable(final int port) {

		ServerSocket ss = null;
		DatagramSocket ds = null;

		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (final IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (final Exception e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}

	public static int randomPort() throws Exception {

		final ServerSocket s = new ServerSocket(0);
		final int port = s.getLocalPort();
		s.close();
		Thread.sleep(100);

		return port;

	}

}
