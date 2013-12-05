package com.barchart.util.common.manifest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.jar.Manifest;

import org.junit.Test;

public class TestManifestUtilities {

	@Test()
	public void manifestReference() throws Exception {

		final File file = new File("src/test/resources/case-01/MANIFEST.MF");

		final InputStream input = new FileInputStream(file);

		final Manifest manifest = new Manifest(input);

		final String version = manifest.getMainAttributes().getValue(
				"Bundle-Version");

		assertNotNull(version);

	}

}
