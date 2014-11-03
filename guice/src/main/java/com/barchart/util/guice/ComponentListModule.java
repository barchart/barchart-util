package com.barchart.util.guice;

import java.lang.reflect.ParameterizedType;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Types;

public final class ComponentListModule extends AbstractModule {

	private static final Logger logger = LoggerFactory.getLogger(ComponentListModule.class);

	private final Injector componentInjector;

	public ComponentListModule(Injector componentInjector) {
		this.componentInjector = componentInjector;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void configure() {
		Multimap<TypeLiteral<?>, Key<Object>> typeMap = HashMultimap.create();
		for (Key<?> key : componentInjector.getAllBindings().keySet()) {
			typeMap.put(key.getTypeLiteral(), (Key<Object>) key);
		}
		bindCollections(typeMap);
	}

	private void bindCollections(Multimap<TypeLiteral<?>, Key<Object>> typeToInstanceMap) {
		
		
		
		for (TypeLiteral<?> bindType : typeToInstanceMap.keys()) {
			logger.info("Binding collection for List<" + bindType + ">");
			ParameterizedType genericListType = Types.newParameterizedType(List.class, bindType.getType());
			TypeLiteral<?> typeLiteral = TypeLiteral.get(genericListType);
			@SuppressWarnings("unchecked")
			AnnotatedBindingBuilder<Object> bindingBuilder = (AnnotatedBindingBuilder<Object>) bind(typeLiteral);
			bindingBuilder.toProvider(new ListProvider(typeToInstanceMap.get(bindType)));
		}
	}

	private final class ListProvider implements Provider<Object> {

		private final ArrayList<Key<Object>> keyList;

		public ListProvider(Collection<Key<Object>> collection) {
			this.keyList = new ArrayList<Key<Object>>(collection);
		}

		@Override
		public Object get() {
			return new AbstractList<Object>() {

				@Override
				public Object get(int index) {
					Key<Object> key = keyList.get(index);
					return componentInjector.getInstance(key);
				}

				@Override
				public int size() {
					return keyList.size();
				}
			};
		}

	}

}
