package com.barchart.util.guice.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.barchart.util.guice.ValueConverter;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableList.Builder;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public final class BasicValueConverters {

	private final List<ValueConverter> list;

	public BasicValueConverters() {
		Builder<ValueConverter> builder = ImmutableList.builder();
		builder.add(new StringConverter());
		builder.add(new ConfigConverter());
		builder.add(new BooleanConverter());
		builder.add(new ByteConverter());
		builder.add(new ShortConverter());
		builder.add(new IntegerConverter());
		builder.add(new LongConverter());
		builder.add(new FloatConverter());
		builder.add(new DoubleConverter());
		builder.add(new StringListConverter());
		builder.add(new BooleanListConverter());
		builder.add(new ConfigListConverter());
		builder.add(new ByteListConverter());
		builder.add(new ShortListConverter());
		builder.add(new IntegerListConverter());
		builder.add(new LongListConverter());
		builder.add(new FloatListConverter());
		builder.add(new DoubleListConverter());
		this.list = builder.build();
	}

	public static final class StringConverter implements ValueConverter {
		@Override
		public String convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.STRING ? (String) value.unwrapped() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(String.class);
		}
	}

	public static final class ConfigConverter implements ValueConverter {
		@Override
		public Config convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.OBJECT ? value.atPath("tmp").getConfig("tmp") : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Config.class);
		}
	}

	public static final class BooleanConverter implements ValueConverter {
		@Override
		public Boolean convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.BOOLEAN ? (Boolean) value.unwrapped() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Boolean.class);
		}
	}

	public static final class ByteConverter implements ValueConverter {
		@Override
		public Byte convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.NUMBER ? ((Number) value.unwrapped()).byteValue() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Byte.class);
		}
	}

	public static final class ShortConverter implements ValueConverter {
		@Override
		public Short convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.NUMBER ? ((Number) value.unwrapped()).shortValue() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Short.class);
		}
	}

	public static final class IntegerConverter implements ValueConverter {
		@Override
		public Integer convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.NUMBER ? ((Number) value.unwrapped()).intValue() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Integer.class);
		}
	}

	public static final class LongConverter implements ValueConverter {
		@Override
		public Long convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.NUMBER ? ((Number) value.unwrapped()).longValue() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Long.class);
		}
	}

	public static final class FloatConverter implements ValueConverter {
		@Override
		public Float convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.NUMBER ? ((Number) value.unwrapped()).floatValue() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Float.class);
		}
	}

	public static final class DoubleConverter implements ValueConverter {
		@Override
		public Double convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.NUMBER ? ((Number) value.unwrapped()).doubleValue() : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Double.class);
		}
	}

	public static final class StringListConverter implements ValueConverter {
		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? buildListForType(value, String.class) : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, String.class));
		}
	}

	public static final class BooleanListConverter implements ValueConverter {
		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? buildListForType(value, Boolean.class) : null;
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Boolean.class));
		}
	}

	public static final class ConfigListConverter implements ValueConverter {
		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? getConfigList(value) : null;
		}

		private Object getConfigList(ConfigValue value) {
			@SuppressWarnings("unchecked")
			List<Object> unwrapped = (List<Object>) value.unwrapped();
			if (unwrapped.isEmpty() || unwrapped.get(0) instanceof HashMap) {
				return value.atKey("tmp").getConfigList("tmp");
			} else {
				return null;
			}
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Config.class));
		}
	}

	public static final class ByteListConverter implements ValueConverter {

		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? transform(buildListForType(value, Number.class)) : null;
		}

		private List<Byte> transform(List<Number> list) {
			return list == null ? null : Lists.transform(list, new Function<Number, Byte>() {
				@Override
				public Byte apply(Number input) {
					return input.byteValue();
				}
			});
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Byte.class));
		}

	}

	public static final class ShortListConverter implements ValueConverter {

		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? transform(buildListForType(value, Number.class)) : null;
		}

		private List<Short> transform(List<Number> list) {
			return list == null ? null : Lists.transform(list, new Function<Number, Short>() {
				@Override
				public Short apply(Number input) {
					return input.shortValue();
				}
			});
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Short.class));
		}

	}

	public static final class IntegerListConverter implements ValueConverter {

		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? transform(buildListForType(value, Number.class)) : null;
		}

		private List<Integer> transform(List<Number> list) {
			return list == null ? null : Lists.transform(list, new Function<Number, Integer>() {
				@Override
				public Integer apply(Number input) {
					return input.intValue();
				}
			});
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Integer.class));
		}

	}

	public static final class LongListConverter implements ValueConverter {

		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? transform(buildListForType(value, Number.class)) : null;
		}

		private List<Long> transform(List<Number> list) {
			return list == null ? null : Lists.transform(list, new Function<Number, Long>() {
				@Override
				public Long apply(Number input) {
					return input.longValue();
				}
			});
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Long.class));
		}

	}

	public static final class FloatListConverter implements ValueConverter {

		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? transform(buildListForType(value, Number.class)) : null;
		}

		private List<Float> transform(List<Number> list) {
			return list == null ? null : Lists.transform(list, new Function<Number, Float>() {
				@Override
				public Float apply(Number input) {
					return input.floatValue();
				}
			});
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Float.class));
		}

	}

	public static final class DoubleListConverter implements ValueConverter {

		@Override
		public Object convert(ConfigValue value) {
			return value.valueType() == ConfigValueType.LIST ? transform(buildListForType(value, Number.class)) : null;
		}

		private List<Double> transform(List<Number> list) {
			return list == null ? null : Lists.transform(list, new Function<Number, Double>() {
				@Override
				public Double apply(Number input) {
					return input.doubleValue();
				}
			});
		}

		@Override
		public TypeLiteral<?> getBindingType() {
			return TypeLiteral.get(Types.newParameterizedType(List.class, Double.class));
		}

	}

	@SuppressWarnings("unchecked")
	private static final <T> List<T> buildListForType(ConfigValue value, Class<T> type) {
		List<T> result = new ArrayList<T>();
		for (Object object : (List<Object>) value.unwrapped()) {
			if (type.isAssignableFrom(object.getClass())) {
				result.add((T) object);
			} else {
				return null;
			}
		}
		return result;
	}

	public List<ValueConverter> getList() {
		return list;
	}

}
