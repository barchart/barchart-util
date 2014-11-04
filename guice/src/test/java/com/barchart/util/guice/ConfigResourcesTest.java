package com.barchart.util.guice;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

@RunWith(Enclosed.class)
public class ConfigResourcesTest {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ConfigResourcesTest.class);

	public static final class ClassPathResourcesTest {

		private ConfigResources configResources;

		@Before
		public void setup() throws IOException {
			this.configResources = new ClassPathResources();
		}

		@Test
		public void listResources() throws IOException {
			checkListResources(configResources);
		}

		@Test
		public void readConfig() throws Exception {
			checkReadConfig(configResources);
		}

		@Test
		public void readConfigOrigin() throws Exception {
			checkReadConfigOrigin(configResources);
		}

		@Test
		public void readResource1() throws Exception {
			checkReadResource1(configResources);
		}

		@Test
		public void readResource2() throws Exception {
			checkReadResource2(configResources);
		}

		@Test(expected = RuntimeException.class)
		public void testBadResource() throws Exception {
			checkBadResource(configResources);
		}

	}

	public static final class DirectoryResourcesTest {

		private ConfigResources configResources;

		@Before
		public void setup() throws IOException {
			this.configResources = new DirectoryResources(new File("src/test/resources/META-INF/conf"));
		}

		@Test
		public void listResources() throws IOException {
			checkListResources(configResources);
		}

		@Test
		public void readConfig() throws Exception {
			checkReadConfig(configResources);
		}

		@Test
		public void readConfigOrigin() throws Exception {
			checkReadConfigOrigin(configResources);
		}

		@Test
		public void readResource1() throws Exception {
			checkReadResource1(configResources);
		}

		@Test
		public void readResource2() throws Exception {
			checkReadResource2(configResources);
		}

		@Test(expected = RuntimeException.class)
		public void testBadResource() throws Exception {
			checkBadResource(configResources);
		}

	}

	private static void checkListResources(ConfigResources configResources) {
		List<String> resources = configResources.listResources();
		assertEquals(3, resources.size());
		assertTrue(resources.contains("application.conf"));
		assertTrue(resources.contains("file1.txt"));
		assertTrue(resources.contains("dir/file2.txt"));
	}

	private static void checkReadConfig(ConfigResources configResources) throws Exception {
		Config config = configResources.readConfig("application.conf");
		assertTrue(config.getBoolean("testing"));
	}

	public static void checkReadConfigOrigin(ConfigResources configResources) throws Exception {
		Config config = configResources.readConfig("application.conf");
		assertTrue(config.origin().filename().endsWith((".conf")));
	}

	private static void checkReadResource1(ConfigResources configResources) throws Exception {
		String str = configResources.readResource("file1.txt");
		assertEquals("testing1", str);
	}

	private static void checkReadResource2(ConfigResources configResources) throws Exception {
		String str = configResources.readResource("dir/file2.txt");
		assertEquals("testing2", str);
	}

	private static void checkBadResource(ConfigResources configResources) throws Exception {
		configResources.readResource("doesntexist");
	}
}
