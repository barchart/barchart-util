package com.barchart.util.hocon;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.hocon.types.BooleanAdapter;
import com.barchart.util.hocon.types.ByteAdapter;
import com.barchart.util.hocon.types.DoubleAdapter;
import com.barchart.util.hocon.types.FloatAdapter;
import com.barchart.util.hocon.types.IntegerAdapter;
import com.barchart.util.hocon.types.LongAdapter;
import com.barchart.util.hocon.types.ShortAdapter;
import com.barchart.util.hocon.types.StringAdapter;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

public class HoconProxyLoader {

	private static final Logger logger = LoggerFactory.getLogger(HoconProxyLoader.class);

	private final Map<String, TypeAdapter<?>> typeAdapters;

	private final NameMorpher nameMorpher;

	public HoconProxyLoader() {
		this.nameMorpher = new DefaultNameMorpher();
		this.typeAdapters = new HashMap<String, TypeAdapter<?>>();
		populateDefaultTypes();
	}

	private void populateDefaultTypes() {
		registerTypeAdapter(byte.class, new ByteAdapter());
		registerTypeAdapter(short.class, new ShortAdapter());
		registerTypeAdapter(int.class, new IntegerAdapter());
		registerTypeAdapter(long.class, new LongAdapter());
		registerTypeAdapter(float.class, new FloatAdapter());
		registerTypeAdapter(double.class, new DoubleAdapter());
		registerTypeAdapter(boolean.class, new BooleanAdapter());

		registerTypeAdapter(Byte.class, new ByteAdapter());
		registerTypeAdapter(Short.class, new ShortAdapter());
		registerTypeAdapter(Integer.class, new IntegerAdapter());
		registerTypeAdapter(Long.class, new LongAdapter());
		registerTypeAdapter(Float.class, new FloatAdapter());
		registerTypeAdapter(Double.class, new DoubleAdapter());
		registerTypeAdapter(Boolean.class, new BooleanAdapter());

		registerTypeAdapter(String.class, new StringAdapter());
	}

	public <T> void registerTypeAdapter(Class<T> clazz, TypeAdapter<T> typeAdapter) {
		typeAdapters.put(clazz.getName(), typeAdapter);
	}

	public <T> T load(Class<T> clazz, File hoconFile) {
		Config config = ConfigFactory.parseFile(hoconFile);

		InvocationHandler handler = new InvocationHandlerImpl<T>(clazz, config);
		return Reflection.newProxy(clazz, handler);
	}

	private final class InvocationHandlerImpl<T> implements InvocationHandler {

		private final ImmutableMap<String, Object> resultMap;

		public InvocationHandlerImpl(Class<T> clazz, Config config) {
			this.resultMap = buildResults(clazz, config);
		}

		private ImmutableMap<String, Object> buildResults(Class<T> clazz, Config config) {
			ConfigObject root = config.root();
			ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

			List<String> missingPaths = new ArrayList<String>();
			for (Method m : clazz.getMethods()) {
				String methodName = m.getName();
				String path = nameMorpher.getConfigPath(methodName);
				TypeAdapter<?> adapter = getAdapter(m.getReturnType());
				ConfigValue configValue = root.get(path);
				if (configValue == null) {
					missingPaths.add(path);
				} else {
					Object convertedValue = adapter.convertValue(configValue);
					if (convertedValue == null) {
						throw new HoconProxyException(adapter.getClass() + " returned null for Hocon value " + configValue);
					}
					builder.put(methodName, convertedValue);
				}
			}

			if (!missingPaths.isEmpty()) {
				throw new HoconProxyException("No configuration value at paths: " + missingPaths);
			}

			return builder.build();
		}

		private TypeAdapter<?> getAdapter(Class<?> returnType) {
			TypeAdapter<?> typeAdapter = typeAdapters.get(returnType.getName());
			if (typeAdapter == null) {
				throw new IllegalStateException("No TypeAdapter available for " + returnType.getName());
			}
			return typeAdapter;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return resultMap.get(method.getName());
		}

	}

}
