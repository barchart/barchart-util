package com.barchart.util.jmx;

/**
 * Service implementation.
 */
public class BenchProvider implements BenchProviderMBean {

	@Override
	public void sayHello() {
		System.out.println("hello, world");
	}

	@Override
	public int add(final int x, final int y) {
		return x + y;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getCacheSize() {
		return this.cacheSize;
	}

	@Override
	public synchronized void setCacheSize(final int size) {
		this.cacheSize = size;

		System.out.println("Cache size now " + this.cacheSize);
	}

	private final String name = "Reginald";
	private int cacheSize = DEFAULT_CACHE_SIZE;
	private static final int DEFAULT_CACHE_SIZE = 200;

}
