package com.barchart.util.guice;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

class DirectoryResources implements ConfigResources {

	private static final Logger logger = LoggerFactory.getLogger(DirectoryResources.class);

	private final File directory;

	public DirectoryResources(File directory) {
		if (!directory.exists()) {
			throw new RuntimeException("Does not exist: " + directory);
		}
		if (!directory.isDirectory()) {
			throw new RuntimeException("Not a directory: " + directory);
		}
		this.directory = directory;
	}

	@Override
	public String readResource(String resourceName) throws Exception {
		logger.info("Reading file resource: " + resourceName);
		return Files.toString(getFile(resourceName), StandardCharsets.UTF_8);
	}

	@Override
	public Config readConfig(String resourceName) throws Exception {
		return ConfigFactory.parseFile(getFile(resourceName)).resolve();
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
		List<File> list = addFilesInDirectory(directory, new ArrayList<File>());
		return Lists.transform(list, new Function<File, String>() {

			final URI directoryURI = directory.toURI();

			@Override
			public String apply(File input) {
				return directoryURI.relativize(input.toURI()).getPath();
			}

		});
	}

	private List<File> addFilesInDirectory(File directory, List<File> files) {
		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				files.addAll(addFilesInDirectory(f, new ArrayList<File>()));
			} else {
				files.add(f);
			}
		}
		return files;
	}

	@Override
	public String getPathDescription() {
		return directory.getAbsolutePath();
	}

	@Override
	public List<Config> readAllConfigs(String filenameExtension) throws Exception {
		List<Config> list = new ArrayList<Config>();
		for (String resource : listResources()) {
			if (resource.endsWith(filenameExtension)) {
				list.add(readConfig(resource));
			}
		}
		return list;
	}

	@Override
	public String toString() {
		return "DirectoryResources: path: " + getPathDescription();
	}
}
