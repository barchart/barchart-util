package com.barchart.util.common.jmx;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper for JMX.
 * <p>
 * Uses default JVM MX bean attribute convention.
 * <li>type == instance class
 * <li>name == instance id
 */
public class HelpJMX {

	private static final Logger logger = LoggerFactory.getLogger(HelpJMX.class);

	/** Instance name attribute. */
	public static final String ATTR_NAME = "name";

	/** Instance type attribute. */
	public static final String ATTR_TYPE = "type";

	/** Default JMX server port. */
	public static final int JMX_PORT_DEFAULT = 50000;

	/**
	 * Collect all implemented interfaces, including nested types.
	 */
	public static void collectInterfaces(Class<?> klaz, final List<Class<?>> typeList) {

		while (klaz != null) {

			final Class<?>[] typeArray = klaz.getInterfaces();

			for (int i = 0; i < typeArray.length; i++) {
				if (!typeList.contains(typeArray[i])) {
					typeList.add(typeArray[i]);
					collectInterfaces(typeArray[i], typeList);
				}
			}

			klaz = klaz.getSuperclass();

		}

	}

	/**
	 * Verify if instance is MBean or MXBean.
	 */
	public static boolean isMBean(final Object instance) {

		if (instance == null) {
			return false;
		}

		// final List<Class<?>> typeList = new ArrayList<Class<?>>();
		// collectInterfaces(instance.getClass(), typeList);

		final Class<?>[] typeList = instance.getClass().getInterfaces();

		for (final Class<?> type : typeList) {
			final String name = type.getName();
			if (name.endsWith("MBean") || name.endsWith("MXBean")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Singleton MX bean name based on instance class only.
	 */
	public static ObjectName name(final Object instance) {
		try {

			final Class<?> klaz = instance.getClass();

			final String domain = klaz.getPackage().getName();

			final ObjectName name = new ObjectName(domain, ATTR_TYPE, klaz.getSimpleName());

			return name;

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Multiton MX bean name based on instance class and instance id.
	 */
	public static ObjectName name(final Object instance, final String id) {
		try {

			final Class<?> klaz = instance.getClass();

			final String domain = klaz.getPackage().getName();

			final Hashtable<String, String> table = new Hashtable<String, String>();
			table.put(ATTR_TYPE, klaz.getSimpleName());
			table.put(ATTR_NAME, id);

			final ObjectName name = new ObjectName(domain, table);

			return name;

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Singleton MX bean name based on domain/type.
	 */
	public static ObjectName name(final String domain, final String type) {
		try {

			final ObjectName name = new ObjectName(domain, ATTR_TYPE, type);

			return name;

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Multiton MX bean name based on domain/type/id.
	 */
	public static ObjectName name(final String domain, final String type, final String id) {
		try {

			final Hashtable<String, String> table = new Hashtable<String, String>();
			table.put(ATTR_TYPE, type);
			table.put(ATTR_NAME, id);

			final ObjectName name = new ObjectName(domain, table);

			return name;

		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Register singleton MX bean with instance class.
	 */
	public static void register(final Object instance) {
		try {
			final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.registerMBean(instance, name(instance));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Register multiton MX bean with instance type and id.
	 */
	public static void register(final Object instance, final String id) {
		try {
			final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.registerMBean(instance, name(instance, id));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Register singleton MX bean with instance class.
	 */
	public static void register(final Object instance, final String domain, final String type, final String id) {
		try {
			final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.registerMBean(instance, name(domain, type, id));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Unregister singleton MX bean.
	 */
	public static void unregister(final Object instance) {
		try {
			final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.unregisterMBean(name(instance));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Unregister multiton MX bean.
	 */
	public static void unregister(final Object instance, final String id) {
		try {
			final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.unregisterMBean(name(instance, id));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Unregister multiton MX bean.
	 */
	public static void unregister(final Object instance, final String domain, final String type, final String id) {
		try {
			final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
			server.unregisterMBean(name(domain, type, id));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static int findAvailablePort(int defaultPort) {
		int port = -1;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(defaultPort);
			port = serverSocket.getLocalPort();
		} catch (IOException e) {
			// Get next free port
			port = getSystemPort();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException ignore) {
				}
			}
		}
		return port;
	}

	private static int getSystemPort() {
		ServerSocket serverSocket = null;
		int port = -1;
		try {
			serverSocket = new ServerSocket(0);
			port = serverSocket.getLocalPort();
		} catch (IOException e) {
			// should not happen
			throw new IllegalStateException("Could not get system port");
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException ignore) {
				}
			}
		}
		return port;
	}

	/*
	 * Starts a JMX server on "localhost" and the default jmx port. If the
	 * default port is not available then a system assigned port will be used.
	 * 
	 * Uses the "jmxmp" protocol only.
	 */
	public static JMXConnectorServer startJmxConnectionServerDefault() {
		int port = findAvailablePort(JMX_PORT_DEFAULT);
		return startJmxConnectionServer(null, port);
	}

	/*
	 * Starts a JMX server on the interface "host".
	 */
	public static JMXConnectorServer startJmxConnectionServerDefault(String host) {
		int port = findAvailablePort(JMX_PORT_DEFAULT);
		return startJmxConnectionServer(host, port);
	}

	public static JMXConnectorServer startJmxConnectionServer(String host, int port) {
		JMXConnectorServer cs = null;
		JMXServiceURL url = null;
		try {
			MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
			/*
			 * Defaults to "jmxmp" protocol, which requires the
			 * jmxremote_optional*.jar
			 */
			url = new JMXServiceURL(null, host, port);
			cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
			cs.start();
		} catch (Exception e) {
			logger.warn("Could not start JMX client server: " + e.getMessage());
			return null;
		}
		logger.info("Started JMX client server at: " + url);
		return cs;
	}
}
