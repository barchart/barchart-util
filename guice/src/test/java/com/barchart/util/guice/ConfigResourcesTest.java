package com.barchart.util.guice;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
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
		public void listResources() throws Exception {
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

		@Test
		public void readAllConfigs() throws Exception {
			checkReadAllConfigs(configResources);
		}


	}

	public static final class DirectoryResourcesTest {

		private ConfigResources configResources;

		@Before
		public void setup() throws IOException {
			this.configResources = new DirectoryResources(new File("src/test/resources/META-INF/conf"));
		}

		@Test
		public void listResources() throws Exception {
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

		@Test
		public void readAllConfigs() throws Exception {
			checkReadAllConfigs(configResources);
		}

	}

	public static final class MergedResourcesTest {

		private ConfigResources configResources;

		@Before
		public void setup() throws IOException {
			this.configResources = new MergedResources(
					new DirectoryResources(new File("src/test/resources/META-INF/conf2")),
					new ClassPathResources());
		}

		@Test
		public void listResources() throws Exception {
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

		@Test
		public void readAllConfigs() throws Exception {
			checkReadAllConfigs(configResources);
		}

		@Test
		public void checkOverride() throws Exception {
			final Config c = configResources.readConfig("application.conf");
			assertEquals("other", c.getString("field"));
		}

	}

	private static void checkListResources(final ConfigResources configResources) throws Exception {
		final List<String> resources = configResources.listResources();
		assertEquals(3, resources.size());
		assertTrue(resources.contains("application.conf"));
		assertTrue(resources.contains("file1.txt"));
		assertTrue(resources.contains("dir/file2.txt"));
	}

	private static void checkReadConfig(final ConfigResources configResources) throws Exception {
		final Config config = configResources.readConfig("application.conf");
		assertTrue(config.getBoolean("testing"));
	}

	public static void checkReadConfigOrigin(final ConfigResources configResources) throws Exception {
		final Config config = configResources.readConfig("application.conf");
		assertTrue(Filetypes.getOriginName(config).endsWith((".conf")));
	}

	private static void checkReadResource1(final ConfigResources configResources) throws Exception {
		final String str = configResources.readResource("file1.txt");
		assertEquals("testing1", str);
	}

	private static void checkReadResource2(final ConfigResources configResources) throws Exception {
		final String str = configResources.readResource("dir/file2.txt");
		assertEquals("testing2", str);
	}

	private static void checkBadResource(final ConfigResources configResources) throws Exception {
		configResources.readResource("doesntexist");
	}

	private static void checkReadAllConfigs(final ConfigResources configResources) throws Exception {
		final List<Config> configs = configResources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION);
		assertEquals(1, configs.size());
		assertEquals("application", Filetypes.getSimpleName(configs.get(0)));

	}
}
