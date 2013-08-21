package com.barchart.util.jmx;

/**
 * Service interface.
 */
public interface BenchService {

	void sayHello();

	int add(int x, int y);

	String getName();

	int getCacheSize();

	void setCacheSize(int size);

}
