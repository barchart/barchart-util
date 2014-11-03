package com.barchart.util.guice.justguice;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;

// Tests to figure out guice reacts to some conditions.
public class JustGuice {

	@Test
	public void testMultibinderWithSingletons() {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(Impl1.class).in(Singleton.class);
				bind(Impl2.class).in(Singleton.class);
				bind(IFace.class).annotatedWith(Names.named("impl1")).to(Impl1.class);
				bind(IFace.class).annotatedWith(Names.named("impl2")).to(Impl2.class);
				Multibinder<IFace> setBinder = Multibinder.newSetBinder(binder(), IFace.class);
				setBinder.addBinding().to(Impl1.class);
				setBinder.addBinding().to(Impl2.class);
			}
		});

//		App instance = injector.getInstance(App.class);
//		instance.impl1.go();
//		instance.impl2.go();
//		for (IFace iface : instance.set) {
//			iface.go();
//		}

	}

	public static final class App {
		@Inject
		@Named("impl1")
		public IFace impl1;

		@Inject
		@Named("impl2")
		public IFace impl2;
		
		@Inject
		public Set<IFace> set;
	}

	public interface IFace {
		public void go();
	}

	public static final class Impl1 implements IFace {

		@Override
		public void go() {
			System.out.println("Impl1: " + hashCode());
		}
	}

	public static final class Impl2 implements IFace {

		@Override
		public void go() {
			System.out.println("Impl2: " + hashCode());
		}
	}

}
