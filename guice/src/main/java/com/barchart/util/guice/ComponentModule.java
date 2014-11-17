package com.barchart.util.guice;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;

final class ComponentModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ComponentModule.class);

	private static final String COMPONENT_TYPE = "type";

	private static final String COMPONENT_NAME = "name";

	@Inject
	private ConfigResources resources;

	@Inject
	private ValueConverterTool valueConverterTool;

	@Inject
	private AnnotationScanner annotationScanner;

	@Inject
	private ComponentScope componentScope;

	@Inject
	private Injector injector;

	public ComponentModule() {
	}

	@Override
	protected void configure() {
		try {
			ImmutableMultimap<Class<?>, Config> componentClassToConfigMap = loadComponentConfigs();
			ImmutableMultimap<Class<?>, TypeLiteral<?>> componentClassToBindingType = determineBindingTypes(componentClassToConfigMap.keySet());
			ImmutableSet<TypeLiteral<?>> noNameEligibleBindingTypes = determineNoNameEligibleBindingTypes(componentClassToConfigMap,
					componentClassToBindingType);
			HashMultiset<TypeLiteral<?>> bindingTypeCounter = HashMultiset.create();
			for (Class<?> componentClass : componentClassToConfigMap.keySet()) {
				ImmutableCollection<Config> configs = componentClassToConfigMap.get(componentClass);
				ImmutableCollection<TypeLiteral<?>> bindings = componentClassToBindingType.get(componentClass);
				configureComponent(componentClass, configs, bindings, noNameEligibleBindingTypes, bindingTypeCounter);
			}

			bindMultibindings(bindingTypeCounter);
			bindEmptySets(bindingTypeCounter);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private ImmutableSet<TypeLiteral<?>> determineNoNameEligibleBindingTypes(ImmutableMultimap<Class<?>, Config> componentClassToConfigMap,
			ImmutableMultimap<Class<?>, TypeLiteral<?>> componentClassToBindingType) {
		HashMultiset<TypeLiteral<?>> bindingOccurences = countBindingOccurences(componentClassToConfigMap, componentClassToBindingType);
		ImmutableSet.Builder<TypeLiteral<?>> builder = ImmutableSet.builder();
		for (TypeLiteral<?> bindingType : bindingOccurences) {
			if (bindingOccurences.count(bindingType) == 1) {
				builder.add(bindingType);
			}
		}
		return builder.build();
	}

	private HashMultiset<TypeLiteral<?>> countBindingOccurences(ImmutableMultimap<Class<?>, Config> componentClassToConfigMap,
			ImmutableMultimap<Class<?>, TypeLiteral<?>> componentClassToBindingType) {

		HashMultiset<TypeLiteral<?>> multiset = HashMultiset.create();
		for (Class<?> componentClass : componentClassToConfigMap.keySet()) {
			int componentCount = componentClassToConfigMap.get(componentClass).size();
			for (TypeLiteral<?> bindingType : componentClassToBindingType.get(componentClass)) {
				multiset.add(bindingType, componentCount);
			}
		}
		return multiset;
	}

	private void configureComponent(Class<?> componentClass, Collection<Config> configs, Collection<TypeLiteral<?>> bindingTypes,
			ImmutableSet<TypeLiteral<?>> noNameEligibleBindingTypes, HashMultiset<TypeLiteral<?>> bindingTypeCounter) {
		for (Config config : configs) {
			ComponentConfigurationModule configurationModule = new ComponentConfigurationModule(componentClass, config, bindingTypes, bindingTypeCounter,
					noNameEligibleBindingTypes);

			install(configurationModule);
		}
	}

	private ImmutableMultimap<Class<?>, TypeLiteral<?>> determineBindingTypes(Set<Class<?>> componentClasses) {
		ImmutableMultimap.Builder<Class<?>, TypeLiteral<?>> builder = ImmutableMultimap.builder();
		for (Class<?> componentClass : componentClasses) {
			List<TypeLiteral<?>> providedTypes = getProvidedTypes(componentClass);
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
	private List<TypeLiteral<?>> getProvidedTypes(Class<?> componentClass) {
		List<TypeLiteral<?>> list = new ArrayList<TypeLiteral<?>>();
		for (Type interfaceType : componentClass.getGenericInterfaces()) {
			if (ParameterizedType.class.isAssignableFrom(interfaceType.getClass())) {
				ParameterizedType parameterizedType = (ParameterizedType) interfaceType;
				Type rawType = parameterizedType.getRawType();
				if (rawType == javax.inject.Provider.class || rawType == com.google.inject.Provider.class) {
					Type providedType = parameterizedType.getActualTypeArguments()[0];
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
		ImmutableMultimap.Builder<Class<?>, Config> builder = ImmutableMultimap.builder();
		for (Config configFile : resources.readAllConfigs(Filetypes.CONFIG_FILE_EXTENSION)) {
			for (Config componentConfig : getComponentList(configFile)) {
				String type = getType(componentConfig);
				Class<?> componentClass = getComponentClass(type);
				if (componentClass != null) {
					builder.put(componentClass, componentConfig);
				}
			}
		}

		for (Config componentConfig : resources.readAllConfigs(Filetypes.COMPONENT_FILE_EXTENSION)) {
			String type = getType(componentConfig);
			Class<?> componentClass = getComponentClass(type);
			if (componentClass != null) {
				builder.put(componentClass, componentConfig);
			}
		}
		return builder.build();
	}

	private void bindMultibindings(Multiset<TypeLiteral<?>> bindingTypeCounter) {
		for (Multiset.Entry<TypeLiteral<?>> entry : bindingTypeCounter.entrySet()) {
			TypeLiteral<?> bindingType = entry.getElement();
			@SuppressWarnings("unchecked")
			Multibinder<Object> setBinder = (Multibinder<Object>) Multibinder.newSetBinder(binder(), bindingType);
			for (int i = 0; i < entry.getCount(); i++) {
				setBinder.addBinding().to(Key.get(bindingType, indexed(i)));
			}
			logger.info("Created Set<" + bindingType + "> binding with " + entry.getCount() + " component" + (entry.getCount() > 1 ? "s" : ""));
		}
	}

	private void bindEmptySets(HashMultiset<TypeLiteral<?>> bindingTypeCounter) {
		Collection<Class<?>> allComponentClasses = annotationScanner.getClassesAnnotatedWith(Component.class);
		Collection<TypeLiteral<?>> allPotentialBindingTypes = new ArrayList<TypeLiteral<?>>();
		for (Class<?> clazz : allComponentClasses) {
			allPotentialBindingTypes.addAll(CastableTypes.of(clazz));
		}
		for (TypeLiteral<?> bindingType : allPotentialBindingTypes) {
			if (!bindingTypeCounter.contains(bindingType)) {
				Multibinder.newSetBinder(binder(), bindingType);
				logger.info("Created Set<" + bindingType + "> binding with 0 components");
			}
		}
	}

	private Collection<Config> getComponentList(Config configFile) {
		List<Config> list = new ArrayList<Config>();
		if (configFile.hasPath(Filetypes.CONFIG_LIST)) {
			list.addAll(configFile.getConfigList(Filetypes.CONFIG_LIST));
		}
		return list;
	}

	private Class<?> getComponentClass(String componentType) {
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> componentClass : annotationScanner.getClassesAnnotatedWith(Component.class)) {
			Component annotation = componentClass.getAnnotation(Component.class);
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

	private static String getType(Config componentConfig) {
		if (!componentConfig.hasPath(COMPONENT_TYPE)) {
			throw new IllegalStateException("Component does not have type:" + componentConfig);
		}
		return componentConfig.getString(COMPONENT_TYPE);
	}

	private static String getName(Config componentConfig) {
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

		public ComponentConfigurationModule(Class<?> componentClass, Config config, Collection<TypeLiteral<?>> bindingTypes,
				Multiset<TypeLiteral<?>> bindingTypeCounter, Set<TypeLiteral<?>> noNameElgibleBindingTypes) {
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
			bindConfiguration(binder());
			installCustomModule();
			List<TypeLiteral<?>> noNameBindings = new ArrayList<TypeLiteral<?>>();
			for (TypeLiteral<?> bindingType : bindingTypes) {

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
				Component annotation = componentClass.getAnnotation(Component.class);
				Class<? extends Module> customModuleClass = annotation.customModule();
				Injector childInjector = injector.createChildInjector(new AbstractModule() {
					@Override
					protected void configure() {
						bindConfiguration(binder());
					}
				});
				Module customModule = childInjector.getInstance(customModuleClass);
				install(customModule);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private boolean isProvider() {
			return javax.inject.Provider.class.isAssignableFrom(componentClass);
		}

		private Collection<String> classNames(Collection<TypeLiteral<?>> classCollection) {
			return Collections2.transform(classCollection, new Function<TypeLiteral<?>, String>() {
				@Override
				public String apply(TypeLiteral<?> input) {
					return input.toString();
				}
			});
		}

		@SuppressWarnings("unchecked")
		private void bindByName(String name, TypeLiteral<?> bindingType) {
			LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) binder() //
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
		private void bindWithNoName(TypeLiteral<?> bindingType) {
			if (bindingType.getRawType() != componentClass) {
				LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) binder() //
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
		private void exposeToComponentList(TypeLiteral<?> bindingType) {
			int index = bindingTypeCounter.add(bindingType, 1);
			LinkedBindingBuilder<Object> bindingBuilder = (LinkedBindingBuilder<Object>) bind(Key.get(bindingType, indexed(index)));
			if (isProvider()) {
				bindingBuilder.toProvider((Class<? extends javax.inject.Provider<Object>>) componentClass);
			} else {
				bindingBuilder.to(componentClass);
			}

			expose(Key.get(bindingType, indexed(index)));
		}

		private void bindConfiguration(Binder binder) {
			UniqueObjectPathSet objectPaths = new UniqueObjectPathSet();
			for (Entry<String, ConfigValue> entry : config.entrySet()) {
				String configValuePath = entry.getKey();
				objectPaths.add(configValuePath);
				bindConfigValue(binder, configValuePath, entry.getValue());
			}
			bindConfigObjectPaths(binder, objectPaths);
		}

		private void bindConfigValue(Binder binder, String key, ConfigValue value) {
			BindUtil bindUtil = new BindUtil(binder);
			for (Map.Entry<TypeLiteral<?>, Object> entry : valueConverterTool.getConversions(value).entrySet()) {
				TypeLiteral<?> bindingType = entry.getKey();
				Object result = entry.getValue();
				bindUtil.bindInstance(bindingType, "#" + key, result);
			}
		}

		private void bindConfigObjectPaths(Binder binder, UniqueObjectPathSet objectPaths) {
			bindConfigValue(binder, "", config.root());
			for (String objectPath : objectPaths) {
				Config object = config.getConfig(objectPath);
				bindConfigValue(binder, objectPath, object.root());
			}
		}

	}

	private Indexed indexed(int index) {
		return new IndexedImpl(index);
	}
}
