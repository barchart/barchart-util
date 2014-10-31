package com.barchart.util.guice;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.PrivateModule;
import com.google.inject.name.Names;

public class PrivateModuleTest {

	
	
	
	public static final class Component1 {
		@Inject
		@Named("#testfield")
		private String testfield;

		@Override
		public String toString() {
			return "Component1 [testfield=" + testfield + "]";
		}
		
	}
	
	
	public static final class Component2 {
		@Inject
		@Named("#testfield")
		private String testfield;

		@Override
		public String toString() {
			return "Component2 [testfield=" + testfield + "]";
		}

		
		
	}
	
	@Test
	public void testPrivateModules() {
		List<Module> privateModules = new ArrayList<Module>();
		privateModules.add(new PrivateModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("#testfield")).toInstance("hello private module 1");
				bind(Component1.class);
				expose(Component1.class);
			}
		});

		privateModules.add(new PrivateModule() {
			@Override
			protected void configure() {
				bind(String.class).annotatedWith(Names.named("#testfield")).toInstance("hello private module 2");
				bind(Component2.class);
				expose(Component2.class);
			}
		});
		
		
		Injector injector = Guice.createInjector(privateModules);
		System.out.println(injector.getInstance(Component1.class));
		
		System.out.println(injector.getInstance(Component2.class));
	}
	
}
