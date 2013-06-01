package com.barchart.util.value.api;

import java.io.File;
import java.util.Scanner;

import org.junit.Test;

public class TestFactoryLoader {

	static String readText(final File file) throws Exception {
		return new Scanner(file, "UTF-8").useDelimiter("\\A").next();
	}

	@Test(expected = IllegalStateException.class)
	public void missingProvider() throws Exception {

		FactoryLoader.load();

	}

}
