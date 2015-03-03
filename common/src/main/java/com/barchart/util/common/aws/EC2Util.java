/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.common.aws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class EC2Util {

	public enum METADATA {
		SECURITY_GROUPS,
		AVAILABILITY_ZONE
	}

	private static String metadataUrl = "http://169.254.169.254/latest/meta-data/";

	private static EnumMap<METADATA, String> metadataMap = new EnumMap<METADATA, String>(METADATA.class);

	static {
		metadataMap.put(METADATA.SECURITY_GROUPS, "security_groups");
		metadataMap.put(METADATA.AVAILABILITY_ZONE, "placement/availability-zone");
	}

	public static Config getUserData() {

		try {

			final URL url = new URL("http://169.254.169.254/latest/user-data");

			final HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setConnectTimeout(2 * 1000);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "config-reader");
			connection.connect();

			final InputStream input = connection.getInputStream();
			final InputStreamReader reader = new InputStreamReader(input);
			final BufferedReader buffered = new BufferedReader(reader);

			final Config conf = ConfigFactory.parseReader(buffered);

			buffered.close();

			return conf;

		} catch (final Exception e) {
			return ConfigFactory.empty();

		}

	}

	/**
	 * @return
	 * @deprecated This method can be replaced with @code getMetadata()
	 */
	@Deprecated
	public static List<String> getSecurityGroups() {

		final List<String> groups = new ArrayList<String>();

		try {

			final URL url = new URL(
					"http://169.254.169.254/latest/meta-data/security-groups");

			final HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setConnectTimeout(2 * 1000);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "config-reader");
			connection.connect();

			final InputStream input = connection.getInputStream();
			final InputStreamReader reader = new InputStreamReader(input);
			final BufferedReader buffered = new BufferedReader(reader);

			String group;
			while ((group = buffered.readLine()) != null) {
				groups.add(group);
			}

			buffered.close();

		} catch (final Exception e) {
		}

		return groups;

	}

	/**
	 * get metadata based on the category
	 *
	 * @param metadata category
	 * @return
	 */
	public static List<String> getMetadata(METADATA value) {

		final List<String> ret = new ArrayList<String>();

		String category = metadataMap.get(value);

		if (category != null && !category.isEmpty()) {
			try {

				final URL url = new URL(metadataUrl + category);

				final HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();

				connection.setConnectTimeout(2 * 1000);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent", "config-reader");
				connection.connect();

				final InputStream input = connection.getInputStream();
				final InputStreamReader reader = new InputStreamReader(input);
				final BufferedReader buffered = new BufferedReader(reader);

				String s;
				while ((s = buffered.readLine()) != null) {
					ret.add(s);
				}

				buffered.close();

			} catch (final Exception e) {
			}
		}

		return ret;

	}

	/**
	 * Read string from URL.
	 */
	public static String readURL(final String textURL) {

		final StringBuilder text = new StringBuilder(128);

		try {

			final URL url = new URL(textURL);

			final HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setConnectTimeout(2 * 1000);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "config-reader");
			connection.connect();

			final InputStream input = connection.getInputStream();
			final InputStreamReader reader = new InputStreamReader(input);
			final BufferedReader buffered = new BufferedReader(reader);

			String line;
			while ((line = buffered.readLine()) != null) {
				text.append(line);
			}

			buffered.close();

		} catch (final Exception e) {
			text.append(e.getMessage());
		}

		return text.toString();

	}

}
