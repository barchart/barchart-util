package com.barchart.util.guice;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.google.common.io.Files;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

class DirectoryResources implements ConfigResources {

	private final File directory;

	public DirectoryResources(File directory) {
		this.directory = directory;
	}

	@Override
	public String readResource(String resourceName) throws Exception {
		return Files.toString(getFile(resourceName), StandardCharsets.UTF_8);
	}

	@Override
	public Config readConfig(String resourceName) throws Exception {
		return ConfigFactory.parseFile(getFile(resourceName));
	}

	private File getFile(String resourceName) {
		File file = new File(directory, resourceName);
		if (!file.exists()) {
			throw new IllegalArgumentException("No resource with name:" + resourceName);
		}
		return file;
	}

	@Override
	public List<String> listResources() {
		// TODO: Not recursive
		return Arrays.asList(directory.list());
	}

	@Override
	public String getPathDescription() {
		return directory.getAbsolutePath();
	}

}
