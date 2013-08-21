package com.barchart.util.jmx;

import com.barchart.util.jmx.HelpJMX;

public class BenchMain {

	public static void main(final String[] args) throws Exception {

		final BenchProvider singleton = new BenchProvider();

		int count = 0;

		while (true) {

			final BenchProvider multiton = new BenchProvider();

			HelpJMX.register(multiton, "instance-" + count++);

			HelpJMX.register(singleton);
			System.out.println("Register.");
			Thread.sleep(5 * 1000);

			HelpJMX.unregister(singleton);
			System.out.println("UnRegister.");
			Thread.sleep(5 * 1000);

		}

	}

}
