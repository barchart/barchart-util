package com.barchart.util.jmx;

/**
 * JMX interface. <br>
 * Communicates to JDK which methods should be exposed for management.
 * <p>
 * JDK convention: must have
 * <li>name composed of prefix + suffix
 * <li>name prefix: name of the managed instance class
 * <li>name suffix: MBean
 */
public interface BenchProviderMBean extends BenchService {

	/** Expose all methods from parent service interface */

}
