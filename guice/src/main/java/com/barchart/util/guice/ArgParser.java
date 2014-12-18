package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simplistic CLI argument parser with no pre-existing knowledge of expected arguments.
 */
public final class ArgParser {

	private final Map<String, List<String>> parsed;

	public ArgParser() {
		parsed = new HashMap<String, List<String>>();
	}

	public ArgParser parse(final String[] args) {
		new Parser().parse(args);
		System.out.println(parsed);
		return this;
	}

	public boolean has(final String arg) {
		return parsed.containsKey(arg);
	}

	public String option(final String arg) {

		final List<String> values = parsed.get(arg);

		if (values != null && values.size() > 0) {
			return values.get(0);
		}

		return null;

	}

	public List<String> options(final String arg) {
		return parsed.get(arg);
	}

	private void addArg(final String key, String value) {

		if (!parsed.containsKey(key)) {
			parsed.put(key, new ArrayList<String>());
		}

		if (value != null && value.startsWith("\"")) {
			value = value.substring(1, value.length() - 1);
		}

		parsed.get(key).add(value);

	}

	private class Parser {

		private String key = null;
		private String value = null;

		Parser() {

		}

		void parse(final String[] args) {

			for (final String arg : args) {
				next(arg);
			}

			if (key != null || value != null) {
				addArg(key, value);
			}

		}

		private void next(final String arg) {
			if (isValue(arg)) {
				newValue(arg);
			} else if (isLongOpt(arg)) {
				newLongOpt(arg);
			} else if (isKey(arg)) {
				newKey(arg);
			}
		}

		private boolean isValue(final String arg) {
			return (value != null && value.startsWith("\"") && !value.endsWith("\""))
					|| !arg.startsWith("-");
		}

		private void newValue(final String arg) {

			if (value != null) {

				if (valueOpen()) {
					value += arg;
					return;
				}

				addArg(key, value);
				key = null;
				value = null;

			}

			value = arg;

		}

		private boolean valueOpen() {
			return value != null && value.startsWith("\"") && !value.endsWith("\"");
		}

		private boolean isLongOpt(final String arg) {
			return arg.matches("^[-a-zA-Z0-9_]*=");
		}

		private void newLongOpt(final String arg) {
			final String[] pair = arg.split("=");
			newKey(pair[0]);
			newValue(pair[1]);
		}

		private boolean isKey(final String arg) {
			return arg.startsWith("-");
		}

		private void newKey(final String arg) {

			if (key != null || value != null) {
				addArg(key, value);
				key = null;
				value = null;
			}

			key = arg.replaceFirst("^-*", "");
			value = null;

		}

	}

}
