package com.barchart.util.guice;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.encryption.Decrypter;
import com.barchart.util.guice.encryption.EchoDecrypter;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

public class ConfigBinder {

	private static final Logger logger = LoggerFactory.getLogger(ConfigBinder.class);

	@Inject
	private ValueConverterTool valueConverterTool;

	@Inject
	private Decrypter decrypter;

	ConfigBinder() {}

	public ConfigBinder(final ValueConverterTool vct) {
		valueConverterTool = vct;
		decrypter = new EchoDecrypter();
	}

	public void applyBindings(final Binder binder, final Config config) {
		applyBindings(binder, config, "");
	}

	public void applyBindings(final Binder binder, final Config config, final String prefix) {
		final Applicator applicator = new Applicator(binder, config, prefix);
		applicator.apply();
	}

	private final class Applicator {

		private final Binder binder;
		private final Config config;
		private final String prefix;

		public Applicator(final Binder binder, final Config config, final String prefix) {
			this.binder = binder;
			this.config = config;
			this.prefix = prefix;
		}

		public void apply() {
			final BindUtil bindUtil = new BindUtil(binder);
			final UniqueObjectPathSet objectPaths = new UniqueObjectPathSet();
			for (final Entry<String, ConfigValue> entry : config.entrySet()) {
				final String configValuePath = entry.getKey();
				objectPaths.add(configValuePath);
				bindConfigValue(bindUtil, configValuePath, entry.getValue());
			}
			bindConfigObjectPaths(bindUtil, objectPaths);
		}

		private void bindConfigValue(final BindUtil bindUtil, final String key, final ConfigValue value) {

			for (final Map.Entry<TypeLiteral<?>, Object> entry : valueConverterTool.getConversions(value).entrySet()) {
				final TypeLiteral<?> bindingType = entry.getKey();
				final Object result = entry.getValue();
				bindUtil.bindInstance(bindingType, prefix + key, result);
			}

			if (value.unwrapped() instanceof String) {
				try {
					final byte[] decrypted = decrypter.decrypt(value.unwrapped().toString().getBytes());
					if (decrypted != null) {
						bindUtil.bindEncrypted(byte[].class, prefix + key, decrypted);
						try {
							bindUtil.bindEncrypted(String.class, prefix + key, new String(decrypted, "UTF-8"));
						} catch (final UnsupportedEncodingException uee) {
							// Binary value, string decode failed (not necessarily a problem)
						}
					}
				} catch (final Throwable t) {
					logger.warn("Error decrypting configuration: ", t);
				}
			}

		}

		private void bindConfigObjectPaths(final BindUtil bindUtil, final UniqueObjectPathSet objectPaths) {
			bindConfigValue(bindUtil, "", config.root());

			for (final String objectPath : objectPaths) {
				final Config object = config.getConfig(objectPath);
				bindConfigValue(bindUtil, objectPath, object.root());
			}
		}

	}
}