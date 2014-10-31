package com.barchart.util.guice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

public final class ConfigDirectory {

	private final File configDirectory;

	public ConfigDirectory(File configDirectory) {
		this.configDirectory = configDirectory;
	}

	public File getFile(String filename) {
		return new File(configDirectory, filename);
	}

	public File getFile() {
		return configDirectory;
	}

	public List<File> listFiles(FilenameFilter filenameFilter) {
		return Arrays.asList(configDirectory.listFiles(filenameFilter));
	}

	public List<File> listFiles(final String fileExtension) {
		return Arrays.asList(configDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith("." + fileExtension);
			}
		}));
	}

}
