package com.barchart.util.guice;

import java.io.File;

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

}
