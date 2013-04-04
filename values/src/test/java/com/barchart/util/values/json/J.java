/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.util.values.json;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class J {

	private static Logger log = LoggerFactory.getLogger(J.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	static {

		applyMapperPolicy(mapper);

	}

	@SuppressWarnings("deprecation")
	public static void applyMapperPolicy(final ObjectMapper mapper) {

		/* READ: */

		// must annotate fields with @JsonProperty explicitly
		mapper.getDeserializationConfig().disable(
				DeserializationConfig.Feature.AUTO_DETECT_FIELDS);
		// must annotate fields with @JsonProperty explicitly
		mapper.getDeserializationConfig().disable(
				DeserializationConfig.Feature.AUTO_DETECT_SETTERS);
		// must annotate fields with @JsonCerator explicitly
		mapper.getDeserializationConfig().disable(
				DeserializationConfig.Feature.AUTO_DETECT_CREATORS);
		// make all unknown into optional
		mapper.getDeserializationConfig().disable(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

		/* WRITE: */

		// will introduce tabs
		mapper.getSerializationConfig().enable(
				SerializationConfig.Feature.INDENT_OUTPUT);

		// must annotate with @JsonProperty explicitly
		mapper.getSerializationConfig().disable(
				SerializationConfig.Feature.AUTO_DETECT_FIELDS);
		// must annotate with @JsonProperty explicitly
		mapper.getSerializationConfig().disable(
				SerializationConfig.Feature.AUTO_DETECT_GETTERS);
		// must annotate with @JsonProperty explicitly
		mapper.getSerializationConfig().disable(
				SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS);
		// no more empty fields
		mapper.getSerializationConfig().disable(
				SerializationConfig.Feature.WRITE_NULL_PROPERTIES);

	}

	public static void registerModule(Module module) {
		mapper.registerModule(module);
	}

	public static <T> T fromText(String text, Class<T> klaz) {
		try {
			T value = mapper.readValue(text, klaz);
			return value;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public static String intoText(Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

}
