package com.barchart.util.guice;

import static org.junit.Assert.*;

import org.junit.Test;

public class FiletypesTest {

	@Test
	public void stripLinenumbers() {
		String result = Filetypes
				.stripLinenumbersFromOriginDescription("jar:file:/home/git/globex-translator-ddf/target/globex-translator-ddf-3.0.0-SNAPSHOT.jar!/META-INF/conf/application.conf: 1");
		assertEquals("jar:file:/home/git/globex-translator-ddf/target/globex-translator-ddf-3.0.0-SNAPSHOT.jar!/META-INF/conf/application.conf", result);
	}

	@Test
	public void simpleName() {
		String result = Filetypes
				.getSimpleName("jar:file:/home/git/globex-translator-ddf/target/globex-translator-ddf-3.0.0-SNAPSHOT.jar!/META-INF/conf/application.conf: 1");
		assertEquals("application", result);
	}

}
