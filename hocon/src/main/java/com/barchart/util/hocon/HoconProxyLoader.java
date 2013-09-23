package com.barchart.util.hocon;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.Reflection;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValue;

public class HoconProxyLoader {

	private static final Logger logger = LoggerFactory.getLogger(HoconProxyLoader.class);

	private final Map<Type, TypeAdapter<?>> typeAdapters;

	private final NameMorpher nameMorpher;

	public HoconProxyLoader() {
		this.nameMorpher = new DefaultNameMorpher();
		this.typeAdapters = new HashMap<Type, TypeAdapter<?>>();
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

		// registerTypeAdapter(List.class, new ListAdapter(typeAdapters));
	}

	public void registerTypeAdapter(Class<?> clazz, TypeAdapter<?> typeAdapter) {
		typeAdapters.put(clazz, typeAdapter);
	}

	public <T> T load(Class<T> clazz, File hoconFile) {
		Config config = ConfigFactory.parseFile(hoconFile);

		InvocationHandler handler = new InvocationHandlerImpl<T>(clazz, config);
		return Reflection.newProxy(clazz, handler);
	}

	private final class InvocationHandlerImpl<T> implements InvocationHandler {

		private final ImmutableMap<String, Object> resultMap;

		public InvocationHandlerImpl(Class<T> clazz, Config config) {
			this.resultMap = new ResultBuilder<T>(clazz, config).getMap();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return resultMap.get(method.getName());
		}

	}

	private final class ResultBuilder<T> {

		private final ImmutableMap.Builder<String, Object> builder;

		private final Class<T> clazz;

		private final Config config;

		private final List<String> missingPaths;

		public ResultBuilder(Class<T> clazz, Config config) {
			checkClass(clazz);
			this.builder = ImmutableMap.builder();
			this.clazz = clazz;
			this.config = config;
			this.missingPaths = new ArrayList<String>();
		}

		private void checkClass(Class<T> clazz) {
			if (!clazz.isInterface()) {
				throw new HoconProxyException(clazz + " is not an interface.");
			}
		}

		public ImmutableMap<String, Object> getMap() {
			buildResults();
			return builder.build();
		}

		private void buildResults() {
			for (Method m : clazz.getMethods()) {
				processMethod(m);
			}

			if (!missingPaths.isEmpty()) {
				throw new HoconProxyException("No configuration value at paths: " + missingPaths);
			}

		}

		private void processMethod(Method m) {
			boolean wasSpecialCase = handleSpecialCases(m);
			if (!wasSpecialCase) {
				String methodName = m.getName();
				String path = nameMorpher.getConfigPath(methodName);
				TypeAdapter<?> adapter = getAdapter(m.getReturnType());
				if (!config.hasPath(path)) {
					missingPaths.add(path);
				} else {
					ConfigValue configValue = config.getValue(path);
					Object convertedValue = adapter.convertValue(configValue);
					if (convertedValue == null) {
						throw new HoconProxyException(adapter.getClass() + " returned null for Hocon value " + configValue);
					}
					builder.put(methodName, convertedValue);
				}
			}
		}

		private boolean handleSpecialCases(Method m) {
			if (m.getReturnType().getName().equals("java.util.List")) {
				handleList(m);
				return true;
			}
			return false;
		}

		private void handleList(Method m) {
			Type genericReturnType = m.getGenericReturnType();
			if (genericReturnType instanceof ParameterizedType) {
				ParameterizedType pType = (ParameterizedType) genericReturnType;
				Type actualReturnType = pType.getActualTypeArguments()[0];
				TypeAdapter<?> adapter = getAdapter(actualReturnType);
				String path = nameMorpher.getConfigPath(m.getName());
				ConfigList configList = config.getList(path);
				ImmutableList.Builder<Object> resultBuilder = ImmutableList.builder();
				for (int i = 0; i < configList.size(); i++) {
					resultBuilder.add(adapter.convertValue(configList.get(i)));
				}
				builder.put(m.getName(), resultBuilder.build());
			} else {
				throw new HoconProxyException("Method " + m + " has unparameterized list as return type.");
			}

		}

		private TypeAdapter<?> getAdapter(Type returnType) {
			TypeAdapter<?> typeAdapter = typeAdapters.get(returnType);
			if (typeAdapter == null) {
				throw new IllegalStateException("No TypeAdapter available for " + returnType);
			}
			return typeAdapter;
		}

	}

}
