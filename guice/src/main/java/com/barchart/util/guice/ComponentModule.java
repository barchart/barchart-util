package com.barchart.util.guice;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.typesafe.config.Config;

final class ComponentModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ComponentModule.class);

	private static final String COMPONENT_TYPE = "type";

	private static final String COMPONENT_NAME = "name";

	private enum MergePrecedence {

		COMPONENT,
		CONF,
		APPLICATION;

		public static MergePrecedence of(final Config config) {

			if (Filetypes.isDefaultConfigFile(config))
				return APPLICATION;

			if (Filetypes.isConfig(config))
				return CONF;

			return COMPONENT;

		}

	}

	@Inject
	private ConfigResources resources;

	@Inject
	private AnnotationScanner annotationScanner;

	@Inject
	private ComponentScope componentScope;

	@Inject
	private Injector injector;

	@Inject
	private ConfigBinder configBinder;

	public ComponentModule() {
	}

	@Override
	protected void configure() {
		try {
			final ImmutableMultimap<Class<?>, Config> componentClassToConfigMap = loadComponentConfigs();
			final ImmutableMultimap<Class<?>, TypeLiteral<?>> componentClassToBindingType = determineBindingTypes(componentClassToConfigMap.keySet());
			final ImmutableSet<TypeLiteral<?>> noNameEligibleBindingTypes = determineNoNameEligibleBindingTypes(componentClassToConfigMap,
					componentClassToBindingType);
			final HashMultiset<TypeLiteral<?>> bindingTypeCounter = HashMultiset.create();
			for (final Class<?> componentClass : componentClassToConfigMap.keySet()) {
				final ImmutableCollection<Config> configs = componentClassToConfigMap.get(componentClass);
				final ImmutableCollection<TypeLiteral<?>> bindings = componentClassToBindingType.get(componentClass);
				configureComponent(componentClass, configs, bindings, noNameEligibleBindingTypes, bindingTypeCounter);
			}

			bindMultibindings(bindingTypeCounter);
			bindEmptySets(bindingTypeCounter);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ImmutableSet<TypeLiteral<?>> determineNoNameEligibleBindingTypes(final ImmutableMultimap<Class<?>, Config> componentClassToConfigMap,
			final ImmutableMultimap<Class<?>, TypeLiteral<?>> componentClassToBindingType) {
		final HashMultiset<TypeLiteral<?>> bindingOccurences = countBindingOccurences(componentClassToConfigMap, componentClassToBindingType);
		final ImmutableSet.Builder<TypeLiteral<?>> builder = ImmutableSet.builder();
		for (final TypeLiteral<?> bindingType : bindingOccurences) {
			if (bindingOccurences.count(bindingType) == 1) {
				builder.add(bindingType);
			}
		}
		return builder.build();
	}

	private HashMultiset<TypeLiteral<?>> countBindingOccurences(final ImmutableMultimap<Class<?>, Config> componentClassToConfigMap,
			final ImmutableMultimap<Class<?>, TypeLiteral<?>> componentClassToBindingType) {

		final HashMultiset<TypeLiteral<?>> multiset = HashMultiset.create();
		for (final Class<?> componentClass : componentClassToConfigMap.keySet()) {
			final int componentCount = componentClassToConfigMap.get(componentClass).size();
			for (final TypeLiteral<?> bindingType : componentClassToBindingType.get(componentClass)) {
				multiset.add(bindingType, componentCount);
			}
		}
		return multiset;
	}

	private void configureComponent(final Class<?> componentClass, final Collection<Config> configs, final Collection<TypeLiteral<?>> bindingTypes,
			final ImmutableSet<TypeLiteral<?>> noNameEligibleBindingTypes, final HashMultiset<TypeLiteral<?>> bindingTypeCounter) {
		for (final Config config : configs) {
			final ComponentConfigurationModule configurationModule = new ComponentConfigurationModule(componentClass, config, bindingTypes, bindingTypeCounter,
					noNameEligibleBindingTypes);

			install(configurationModule);
		}
	}

	private ImmutableMultimap<Class<?>, TypeLiteral<?>> determineBindingTypes(final Set<Class<?>> componentClasses) {
		final ImmutableMultimap.Builder<Class<?>, TypeLiteral<?>> builder = ImmutableMultimap.builder();
		for (final Class<?> componentClass : componentClasses) {
			final List<TypeLiteral<?>> providedTypes = getProvidedTypes(componentClass);
			if (providedTypes.isEmpty()) {
				// Not a provider
				builder.putAll(componentClass, CastableTypes.of(componentClass));
			} else {
				// Is a provider
				builder.putAll(componentClass, providedTypes);
			}

		}
		return builder.build();
	}

	/*
	 *
	 * If the copmonentClass is a provider, return a list of all types that can
	 * be provided. Otherwise, return the empty list.
	 */
	private List<TypeLiteral<?>> getProvidedTypes(final Class<?> componentClass) {
		final List<TypeLiteral<?>> list = new ArrayList<TypeLiteral<?>>();
		for (final Type interfaceType : componentClass.getGenericInterfaces()) {
			if (ParameterizedType.class.isAssignableFrom(interfaceType.getClass())) {
				final ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
				final Type rawType = parameterizedType.getRawType();
				if (rawType == javax.inject.Provider.class || rawType == com.google.inject.Provider.class) {
					final Type providedType = parameterizedType.getActualTypeArguments()[0];
					logger.info("Found provider for " + providedType);
					if (providedType instanceof Class) {
						list.addAll(Lists.newArrayList(CastableTypes.of((Class<?>) providedType)));
					}
				}
			}
		}
		return list;
	}

	private ImmutableMultimap<Class<?>, Config> loadComponentConfigs() throws Exception {

		// Temporary hash map for merging duplicate type/name combos
		final Map<Key<?>, Config> merged = new HashMap<Key<?>, Config>();

		for (final Config configFile : resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
			for (final Config componentConfig : getComponentList(configFile)) {
				final Key<?> key = getMergeKey(componentConfig);
				if (key != null) {
					merged.put(key, getMergedConfig(merged.get(key), componentConfig));
				}
			}
		}

		for (final Config componentConfig : resources.readAllConfigs(Filetypes.COMPONENT_FILE_EXTENSION)) {
			final Key<?> key = getMergeKey(componentConfig);
			if (key != null) {
				merged.put(key, getMergedConfig(merged.get(key), componentConfig));
			}
		}

		// Add to immutable multimap

		final ImmutableMultimap.Builder<Class<?>, Config> builder = ImmutableMultimap.builder();

		for (final Map.Entry<Key<?>, Config> entry : merged.entrySet()) {
			builder.put(entry.getKey().getTypeLiteral().getRawType(), entry.getValue());
		}

		return builder.build();

	}

	private Key<?> getMergeKey(final Config componentConfig) {

		final String type = getType(componentConfig);
		final String name = getName(componentConfig);
		final Class<?> componentClass = getComponentClass(type);

		if (componentClass != null) {

			if (name == null) {
				return Key.get(componentClass);
			}

			return Key.get(componentClass, Names.named(name));

		}

		return null;

	}

	private Config getMergedConfig(final Config first, final Config second) {

		if (first == null)
			return second;

		if (second == null)
			return first;

		if (MergePrecedence.of(first).ordinal() > MergePrecedence.of(second).ordinal()) {
			return first.withFallback(second);
		} else {
			return second.withFallback(first);
		}

	}

	private void bindMultibindings(final Multiset<TypeLiteral<?>> bindingTypeCounter) {
		for (final Multiset.Entry<TypeLiteral<?>> entry : bindingTypeCounter.entrySet()) {
			final TypeLiteral<?> bindingType = entry.getElement();
			@SuppressWarnings("unchecked")
			final
			Multibinder<Object> setBinder = (Multibinder<Object>) Multibinder.newSetBinder(binder(), bindingType);
			for (int i = 0; i < entry.getCount(); i++) {
				setBinder.addBinding().to(Key.get(bindingType, indexed(i)));
			}
			logger.info("Created Set<" + bindingType + "> binding with " + entry.getCount() + " component" + (entry.getCount() > 1 ? "s" : ""));
		}
	}

	private void bindEmptySets(final HashMultiset<TypeLiteral<?>> bindingTypeCounter) {
		final Collection<Class<?>> allComponentClasses = annotationScanner.getComponentClasses();
		final Collection<TypeLiteral<?>> allPotentialBindingTypes = new ArrayList<TypeLiteral<?>>();
		for (final Class<?> clazz : allComponentClasses) {
			allPotentialBindingTypes.addAll(CastableTypes.of(clazz));
		}
		for (final TypeLiteral<?> bindingType : allPotentialBindingTypes) {
			if (!bindingTypeCounter.contains(bindingType)) {
				Multibinder.newSetBinder(binder(), bindingType);
				logger.info("Created Set<" + bindingType + "> binding with 0 components");
			}
		}
	}

	private Collection<Config> getComponentList(final Config configFile) {
		final List<Config> list = new ArrayList<Config>();
		if (configFile.hasPath(Filetypes.CONFIG_LIST)) {
			list.addAll(configFile.getConfigList(Filetypes.CONFIG_LIST));
		}
		return list;
	}

	private Class<?> getComponentClass(final String componentType) {
		final List<Class<?>> list = new ArrayList<Class<?>>();
		for (final Class<?> componentClass : annotationScanner.getComponentClasses()) {
			final Component annotation = componentClass.getAnnotation(Component.class);
			if (componentType.equals(annotation.value())) {
				list.add(componentClass);
			}
		}
		if (list.size() == 0) {
			// TODO: Throw exception?
			logger.warn("No @Component classes found for configured component type \"" + componentType + "\"");
			return null;
		} else if (list.size() > 1) {
			// TODO: Throw exception?
			logger.warn("Multiple @Component classes found for component type: \"" + componentType + "\".  classes=" + list + ".  Using the first one.");
		}
		return list.get(0);
	}

	private static String getType(final Config componentConfig) {
		if (!componentConfig.hasPath(COMPONENT_TYPE)) {
			throw new IllegalStateException("Component does not have type:" + componentConfig);
		}
		return componentConfig.getString(COMPONENT_TYPE);
	}

	private static String getName(final Config componentConfig) {
		if (!componentConfig.hasPath(COMPONENT_NAME)) {
			return null;
		} else {
			return componentConfig.getString(COMPONENT_NAME);
		}
	}

	private final class ComponentConfigurationModule extends PrivateModule {

		private final Config config;

		private final Class<?> componentClass;

		private final Multiset<TypeLiteral<?>> bindingTypeCounter;

		private final Collection<TypeLiteral<?>> bindingTypes;

		private final Set<TypeLiteral<?>> noNameEligibleBindingTypes;

		private final String type;

		private final String name;

		public ComponentConfigurationModule(final Class<?> componentClass, final Config config, final Collection<TypeLiteral<?>> bindingTypes,
				final Multiset<TypeLiteral<?>> bindingTypeCounter, final Set<TypeLiteral<?>> noNameElgibleBindingTypes) {
			this.type = getType(config);
			this.name = getName(config);
			this.componentClass = componentClass;
			this.bindingTypes = bindingTypes;
			this.config = config;
			this.bindingTypeCounter = bindingTypeCounter;
			this.noNameEligibleBindingTypes = noNameElgibleBindingTypes;
		}

		@Override
		protected void configure() {
			bindScope(PrivateComponentScoped.class, new PrivateComponentScope(componentScope, type, name));
			bind(componentClass).in(PrivateComponentScoped.class);
			configBinder.applyBindings(binder(), config, "#");
			installCustomModule();
			final List<TypeLiteral<?>> noNameBindings = new ArrayList<TypeLiteral<?>>();
			for (final TypeLiteral<?> bindingType : bindingTypes) {

				if (name != null) {
					bindByName(name, bindingType);
				}

				if (noNameEligibleBindingTypes.contains(bindingType)) {
					noNameBindings.add(bindingType);
					// If this component is the only binding for this type,
					// bind it without a name.
					// TODO: What happens if the type is bound by some other
					// module? (Not a component)
					// How can we skip this noname binding in that case?
					bindWithNoName(bindingType);
				}

				exposeToComponentList(bindingType);
			}

			{
				String message = "Found component configuration with type=\"" + type + "\", name=\"" + name + "\", and class=\"" + componentClass.getName()
						+ "\". Binding to: " + classNames(bindingTypes) + ".";
				if (!noNameBindings.isEmpty()) {
					message += "  No-name bindings: " + classNames(noNameBindings);
				}
				logger.info(message);
			}

		}

		private void installCustomModule() {
			try {
				final Component annotation = componentClass.getAnnotation(Component.class);
				final Class<? extends Module> customModuleClass = annotation.customModule();
				final Injector childInjector = injector.createChildInjector(new AbstractModule() {
					@Override
					protected void configure() {
						configBinder.applyBindings(binder(), config, "#");
					}
				});
				final Module customModule = childInjector.getInstance(customModuleClass);
				install(customModule);
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}

		private boolean isProvider() {
			return javax.inject.Provider.class.isAssignableFrom(componentClass);
		}

		private Collection<String> classNames(final Collection<TypeLiteral<?>> classCollection) {
			return Collections2.transform(classCollection, new Function<TypeLiteral<?>, String>() {
				@Override
				public String apply(final TypeLiteral<?> input) {
					return input.toString();
				}
			});
		}

		@SuppressWarnings("unchecked")
		private void bindByName(final String name, final TypeLiteral<?> bindingType) {
			final LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) binder() //
					.withSource(config) //
					.bind(bindingType) //
					.annotatedWith(Names.named(name));
			if (isProvider()) {
				bindingBuilder.toProvider((Class<? extends javax.inject.Provider<Object>>) componentClass);
			} else {
				bindingBuilder.to(componentClass);
			}
			expose(bindingType).annotatedWith(Names.named(name));
		}

		@SuppressWarnings("unchecked")
		private void bindWithNoName(final TypeLiteral<?> bindingType) {
			if (bindingType.getRawType() != componentClass) {
				final LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) binder() //
						.withSource(config) //
						.bind(bindingType);
				if (isProvider()) {
					bindingBuilder.toProvider((Class<? extends javax.inject.Provider<Object>>) componentClass);
				} else {
					bindingBuilder.to(componentClass);
				}
			}
			expose(bindingType);
		}

		@SuppressWarnings("unchecked")
		private void exposeToComponentList(final TypeLiteral<?> bindingType) {
			final int index = bindingTypeCounter.add(bindingType, 1);
			final LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) bind(Key.get(bindingType, indexed(index)));
			if (isProvider()) {
				bindingBuilder.toProvider((Class<? extends javax.inject.Provider<Object>>) componentClass);
			} else {
				bindingBuilder.to(componentClass);
			}

			expose(Key.get(bindingType, indexed(index)));
		}

	}

	private Indexed indexed(final int index) {
		return new IndexedImpl(index);
	}
}
