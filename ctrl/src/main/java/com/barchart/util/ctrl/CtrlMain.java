package com.barchart.util.ctrl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.jmx.JMXConfiguratorMBean;

import com.barchart.util.common.jmx.HelpJMX;

/**
 * Command line configuration utility.
 */
public final class CtrlMain {

	private static final String LOGBACK_MBEAN = "ch.qos.logback.classic:Name=default,Type=ch.qos.logback.classic.jmx.JMXConfigurator";

	private static final Logger logger = LoggerFactory.getLogger(CtrlMain.class);

	@Option(name = "-host", usage = "Server host, default to localhost", required = false)
	private String host = "localhost";

	@Option(name = "-port", usage = "Server's JMX port, defaults to " + HelpJMX.JMX_PORT_DEFAULT, required = false)
	private int port = HelpJMX.JMX_PORT_DEFAULT;

	@Option(name = "-d0", usage = "Debug Level 0 - turn off", required = false)
	private boolean debug0;

	@Option(name = "-d1", usage = "Debug Level 1", required = false)
	private boolean debug1;

	@Option(name = "-d2", usage = "Debug Level 2", required = false)
	private boolean debug2;

	// Server's MBean server
	private MBeanServerConnection mbsc;

	// Client connection
	private JMXConnector jmxc;

	public static void main(String[] args) throws Exception {

		CtrlMain app = new CtrlMain();
		CmdLineParser parser = new CmdLineParser(app);
		try {
			parser.parseArgument(args);
			app.start();
		} catch (CmdLineException e) {
			logger.error("Parser error: " + e.getMessage());
			parser.printUsage(System.out);
		}
	}

	private void start() throws IOException {

		jmxc = HelpJMX.getJmxConnection(host, port);
		if (jmxc == null) {
			return;
		}
		try {
			mbsc = jmxc.getMBeanServerConnection();

			// dumpServerJmxConfig();

			processCommand();

		} catch (IOException e) {
			logger.error("Could not create connection to remote server: " + e.getMessage());
			return;
		} finally {
			if (jmxc != null) {
				jmxc.close();
			}
		}

	}

	private void processCommand() {

		try {
			ObjectName mbeanName = new ObjectName(LOGBACK_MBEAN);
			JMXConfiguratorMBean mbeanProxy = JMX.newMBeanProxy(mbsc, mbeanName,
					ch.qos.logback.classic.jmx.JMXConfiguratorMBean.class, true);

			String rootLevel = mbeanProxy.getLoggerLevel("ROOT");
			logger.info("Current root log level: " + rootLevel);

			if (debug0) {
				// Turn off
				logger.info("Activating info level for ROOT");
				mbeanProxy.setLoggerLevel("ROOT", "info");
				mbeanProxy.setLoggerLevel("log.message", "info");
			} else if (debug1) {
				// Root logger to debug
				logger.info("Activating debug level for ROOT");
				mbeanProxy.setLoggerLevel("ROOT", "debug");
				mbeanProxy.setLoggerLevel("log.message", "info");
			} else if (debug2) {
				// Activate message logger
				logger.info("Activating debug level for ROOT and message logging.");
				mbeanProxy.setLoggerLevel("ROOT", "debug");
				mbeanProxy.setLoggerLevel("log.message", "debug");
			}
		} catch (Exception e) {
			logger.error("Could not process command: " + e.getMessage());
		}
	}

	private void dumpServerJmxConfig() throws IOException {

		String domains[] = mbsc.getDomains();
		Arrays.sort(domains);
		for (String domain : domains) {
			logger.debug("\tDomain = " + domain);
		}

		logger.debug("\tMBeanServer default domain = " + mbsc.getDefaultDomain());

		logger.debug("\nQuery MBeanServer MBeans:");
		Set<ObjectName> names = new TreeSet<ObjectName>(mbsc.queryNames(null, null));
		for (ObjectName name : names) {
			logger.debug("\tObjectName = " + name);
		}

	}
}
