/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.aws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class EC2Util {

	public static Config getUserData() {

		try {

			final URL url = new URL("http://169.254.169.254/latest/user-data");

			final HttpURLConnection connection =
					(HttpURLConnection) url.openConnection();

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

	public static List<String> getSecurityGroups() {

		final List<String> groups = new ArrayList<String>();

		try {

			final URL url =
					new URL(
							"http://169.254.169.254/latest/meta-data/security-groups");

			final HttpURLConnection connection =
					(HttpURLConnection) url.openConnection();

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
}
