package com.barchart.util.jmx;

import java.lang.management.ManagementFactory;
import java.util.Hashtable;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Helper for JMX.
 * <p>
 * Uses default JVM MX bean attribute convention.
 * <li>type == instance class
 * <li>name == instance id
 */
public class HelpJMX {

	/** Instance name attribute. */
	public static final String ATTR_NAME = "name";

	/** Instance type attribute. */
	public static final String ATTR_TYPE = "type";

	/**
	 * Singleton MX bean name based on instance class only.
	 */
	public static ObjectName name(final Object instance) {
		try {

			final Class<?> klaz = instance.getClass();

			final String domain = klaz.getPackage().getName();

			final ObjectName name = new ObjectName(domain, ATTR_TYPE,
					klaz.getSimpleName());

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
	 * Register singleton MX bean with instance class.
	 */
	public static void register(final Object instance) {
		try {
			final MBeanServer server = ManagementFactory
					.getPlatformMBeanServer();
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
			final MBeanServer server = ManagementFactory
					.getPlatformMBeanServer();
			server.registerMBean(instance, name(instance, id));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Unregister singleton MX bean.
	 */
	public static void unregister(final Object instance) {
		try {
			final MBeanServer server = ManagementFactory
					.getPlatformMBeanServer();
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
			final MBeanServer server = ManagementFactory
					.getPlatformMBeanServer();
			server.unregisterMBean(name(instance, id));
		} catch (final Throwable e) {
			throw new RuntimeException(e);
		}
	}

}
