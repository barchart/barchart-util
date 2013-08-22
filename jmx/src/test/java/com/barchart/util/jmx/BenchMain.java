package com.barchart.util.jmx;

/**
 * JMX tester invoker.
 */
public class BenchMain {

	public static void main(final String[] args) throws Exception {

		/**
		 * Static single instance exposed for mangement.
		 */
		final BenchProvider singleton = new BenchProvider();

		int count = 0;

		while (true) {

			/**
			 * Dynamic multiple instances exposed for management.
			 */
			final BenchProvider multiton = new BenchProvider();
			final String mutitonId = "instance-" + count++;
			HelpJMX.register(multiton, mutitonId);
			System.out.println("Multiton Instance: " + mutitonId);

			/**
			 * Enable management for the singleton.
			 */
			HelpJMX.register(singleton);
			System.out.println("Singleton Register.");
			Thread.sleep(5 * 1000);

			/**
			 * Disable management for the singleton.
			 */
			HelpJMX.unregister(singleton);
			System.out.println("Singleton UnRegister.");
			Thread.sleep(5 * 1000);

		}

	}

}
