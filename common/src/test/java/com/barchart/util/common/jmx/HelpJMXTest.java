package com.barchart.util.common.jmx;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HelpJMXTest {

	@Test
	public void findAvailablePort() {
		// Will fail, so dynamic port will be allocated.
		int availablePort = HelpJMX.findAvailablePort(1);
		assertTrue(availablePort > 1);
	}

}
