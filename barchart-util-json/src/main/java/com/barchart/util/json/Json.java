package com.barchart.util.json;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Json {

	private static Logger log = LoggerFactory.getLogger(Json.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	static {

		applyMapperPolicy(mapper);

	}

	public static ObjectMapper getInstance() {
		return mapper;
	}

	@SuppressWarnings("deprecation")
	public static void applyMapperPolicy(final ObjectMapper mapper) {

		/* READ: */

		// it is fine to use "{ a : 'b' }"
		mapper.configure(//
				JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(//
				JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

		// must annotate fields with @JsonProperty explicitly
		mapper.configure(//
				DeserializationConfig.Feature.AUTO_DETECT_FIELDS, false);
		mapper.configure(//
				DeserializationConfig.Feature.AUTO_DETECT_SETTERS, false);
		mapper.configure(//
				DeserializationConfig.Feature.AUTO_DETECT_CREATORS, false);

		// make all unknown into optional
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//

		/* WRITE: */

		// will introduce tabs
		mapper.configure(//
				SerializationConfig.Feature.INDENT_OUTPUT, true);

		// must annotate with @JsonProperty explicitly
		mapper.configure(//
				SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
		mapper.configure(//
				SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
		mapper.configure(//
				SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS, false);

		// no more empty fields
		mapper.configure(//
				SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);

		// will NOT close output NOT owned by the generator.
		mapper.configure(//
				JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

	}

	public static void registerModule(Module module) {
		mapper.registerModule(module);
	}

	public static void applyMapperPolicyIdent(ObjectMapper mapper, boolean on) {
		if (on) {
			mapper.getSerializationConfig().enable(
					SerializationConfig.Feature.INDENT_OUTPUT);
		} else {
			mapper.getSerializationConfig().disable(
					SerializationConfig.Feature.INDENT_OUTPUT);
		}
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

	public static <T> T fromCP(String path, Class<T> klaz) {
		try {
			InputStream input = Json.class.getResourceAsStream(path);
			T value = mapper.readValue(input, klaz);
			input.close();
			return value;
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public static <T> T fromURL(URL url, Class<T> klaz) {
		try {
			T value = mapper.readValue(url, klaz);
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

	public static boolean update(final Object value, final String json) {
		try {
			final ObjectReader reader = mapper.updatingReader(value);
			reader.readValue(json);
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}

	public static final String PROP_NAME = "json";

	public static boolean update(final Object value,
			final Map<String, String> config) {
		final String json = config.get(PROP_NAME);
		return update(value, json);
	}

}
