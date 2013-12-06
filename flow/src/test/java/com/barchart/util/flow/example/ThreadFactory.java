package com.barchart.util.flow.example;

public class ThreadFactory implements java.util.concurrent.ThreadFactory {

	final String name;

	public ThreadFactory(final String name) {
		this.name = name;
	}

	@Override
	public Thread newThread(final Runnable task) {
		final Thread thread = new Thread(task, name);
		thread.setDaemon(true);
		return thread;
	}

}
