package com.barchart.util.guice;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.encryption.Decrypter;
import com.barchart.util.guice.encryption.NullDecrypter;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.PrivateModule;
import com.typesafe.config.Config;

public class DecrypterConfigModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(DecrypterConfigModule.class);

	private static final String CONFIG_PATH = "decrypter";

	@Inject
	private ConfigResources configResources;

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {

		Config decrypterConfig = null;

		try {

			for (final Config config : configResources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
				if (config.hasPath(CONFIG_PATH)) {
					if (decrypterConfig == null || Filetypes.isDefaultConfigFile(config)) {
						decrypterConfig = config.getConfig(CONFIG_PATH);
					}
				}
			}

			if (decrypterConfig != null) {

				final String type = decrypterConfig.getString("type");
				final Class<?> clazz = ClassUtil.forName(type);

				if (Decrypter.class.isAssignableFrom(clazz)) {
					install(new DecrypterPrivateModule((Class<? extends Decrypter>) clazz, decrypterConfig));
					return;
				} else {
					logger.error("Invalid decrypter type hierarchy: " + type);
				}

			}

		} catch (final Exception e) {
			logger.warn("{}", e);
		}

		// Nothing found, fallback to null decrypter
		bind(Decrypter.class).to(NullDecrypter.class).in(Singleton.class);

	}

	private class DecrypterPrivateModule extends PrivateModule {

		private final Class<? extends Decrypter> type;
		private final Config config;

		DecrypterPrivateModule(final Class<? extends Decrypter> type_, final Config config_) {
			type = type_;
			config = config_;
		}

		@Override
		protected void configure() {
			new ConfigBinder(ValueConverterTool.defaultValueConverterTool()).applyBindings(binder(), config, "#");
			bind(Decrypter.class).to(type).in(Singleton.class);
			expose(Decrypter.class);
		}

	}

}