package com.barchart.util.common.manifest;

import java.net.URL;
import java.util.jar.Manifest;

public class ManifestUtilities {

	/**
	 * Bundle manifest.
	 */
	public static Manifest manifest(final Class<?> klaz) throws Exception {

		final String klazFile = klaz.getSimpleName() + ".class";

		final String klazFolder = klaz.getPackage().getName().replace(".", "/");

		final String klazPath = klazFolder + "/" + klazFile;

		final String klazURL = klaz.getResource(klazFile).toString();

		final String manifestURL = klazURL.replace(klazPath,
				"META-INF/MANIFEST.MF");

		return new Manifest(new URL(manifestURL).openStream());

	}

}
