package com.barchart.util.cluster.hazelcast.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.barchart.osgi.component.base.BaseComponent;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

/**
 * Base class for Barchart HoconService-configured components. Mainly provides
 * helper methods for config retrieval.
 *
 * @author jeremy
 *
 */
public abstract class DefaultComponent extends BaseComponent {

	private static DateFormat dateParser = new SimpleDateFormat(
			"yyyy-MM-DD'T'HH:mm:ss.SSS");
	static {
		dateParser.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	protected void processActivate() throws Exception {
		activate();
	}

	@Override
	protected void processModified() throws Exception {
		modified();
	}

	@Override
	protected void processDeactivate() throws Exception {
		deactivate();
	}

	/*
	 * Backwards compatibility
	 */

	protected void activate() throws Exception {
	}

	protected void modified() throws Exception {
	}

	protected void deactivate() throws Exception {
	}

	/*
	 * Config value helpers.
	 */

	/**
	 * Get an object map of string values from the root.
	 */
	public Map<String, String> getConfigMap() {
		return getConfigMap(null);
	}

	/**
	 * Get an object map of string values from the specified path.
	 */
	public Map<String, String> getConfigMap(final String path) {

		final Map<String, String> map = new HashMap<String, String>();

		ConfigObject confObject;
		if (path == null) {
			confObject = configCurrent().root();
		} else if (configCurrent().hasPath(path)) {
			confObject = configCurrent().getObject(path);
		} else {
			return map;
		}

		for (final Map.Entry<String, ConfigValue> e : confObject.entrySet()) {
			if (e.getValue().valueType() == ConfigValueType.STRING) {
				map.put(e.getKey(), (String) e.getValue().unwrapped());
			}
		}

		return map;

	}

	/**
	 * Get a list of string-valued object maps from the specified path.
	 */
	public List<Map<String, String>> getConfigMaps(final String path) {

		final List<Map<String, String>> maps =
				new ArrayList<Map<String, String>>();

		if (!configCurrent().hasPath(path)) {
			return maps;
		}

		for (final ConfigObject v : configCurrent().getObjectList(path)) {

			final Map<String, String> map = new HashMap<String, String>();

			for (final Map.Entry<String, Object> e : v.unwrapped().entrySet()) {
				map.put(e.getKey(), e.getValue().toString());
			}

			maps.add(map);

		}

		return maps;

	}

	/**
	 * Get a string value from the component configuration.
	 */
	public String getConfigString(final String path) {
		try {
			final String value = configCurrent().getString(path);
			if (value != null) {
				return value.trim();
			}
		} catch (final Exception e) {
		}
		return null;
	}

	/**
	 * Get a string value from the component configuration.
	 *
	 * @param value
	 *            The default value to return if none is found
	 */
	public String getConfigString(final String path, final String value) {
		try {
			return first(getConfigString(path), value);
		} catch (final Exception e) {
			return value;
		}
	}

	/**
	 * Get a integer value from the component configuration.
	 *
	 * @param value
	 *            The default value to return if none is found
	 */
	public int getConfigInt(final String path, final int value) {
		try {
			return first(configCurrent().getInt(path), value);
		} catch (final Exception e) {
			return value;
		}
	}

	/**
	 * Get a double value from the component configuration.
	 *
	 * @param value
	 *            The default value to return if none is found
	 */
	public double getConfigDouble(final String path, final double value) {
		try {
			return first(configCurrent().getDouble(path), value);
		} catch (final Exception e) {
			return value;
		}
	}

	/**
	 * Get a boolean value from the component configuration.
	 *
	 * @param value
	 *            The default value to return if none is found
	 */
	public boolean getConfigBool(final String path, final boolean value) {
		try {
			return first(configCurrent().getBoolean(path), value);
		} catch (final Exception e) {
			return value;
		}
	}

	/**
	 * Get a datetime value from the component configuration.
	 *
	 * @param value
	 *            The default value to return if none is found
	 */
	public Date getConfigDate(final String path, final Date value) {
		try {
			return dateParser.parse(configCurrent().getString(path));
		} catch (final Exception e) {
			return value;
		}
	}

	/**
	 * Return the first non-null value in the parameter list.
	 */
	@SafeVarargs
	protected static <T> T first(final T... values) {
		for (final T v : values) {
			if (v != null) {
				return v;
			}
		}
		return null;
	}

}
