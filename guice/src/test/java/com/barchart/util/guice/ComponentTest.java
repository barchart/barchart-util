package com.barchart.util.guice;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
//import javax.inject.Named;

import javax.inject.Named;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.guice.component.Component;
import com.barchart.util.guice.component.Configurator;
import com.google.inject.Injector;

@Component
public class ComponentTest {

	private static final Logger logger = LoggerFactory.getLogger(ComponentTest.class);

	public static final class Main {

		@Inject
		@Named("component1")
		private MyComponent component1;

		@Inject
		@Named("component2")
		private MyComponent component2;

		// @Inject
		private List<MyComponent> components;

		@Override
		public String toString() {
			return "Main [component1=" + component1 + ", component2=" + component2 + ", components=" + components + "]";
		}

	}

	@Component("mycomponent")
	public static final class MyComponent {

		@Inject
		@Named("#name")
		private String name;
		
		@Inject
		@Named("other")
		private OtherComponent other;
		
		MyComponent() {
			logger.info("MyComponent() " + hashCode());
		}

		@Override
		public String toString() {
			return "MyComponent [name=" + name + ", other=" + other + "]";
		}

		
		

	}
	
	
	@Component("othercomponent")
	public static final class OtherComponent {
		
		@Inject
		@Named("#name")
		private String name;

		@Override
		public String toString() {
			return "OtherComponent [name=" + name + "]";
		}
		
		
	}
	

	public static void main(String[] args) {
		logger.info("Starting");
		Configurator configurator = new Configurator(new File("src/test/resources/components"));
		Injector injector = configurator.configureInjector();
		Main main = injector.getInstance(Main.class);
		logger.info("Main: " + main);
		System.out.println("Main:\n" + main.component1.hashCode());
		System.out.println("Main:\n" + main.component2.hashCode());
	}
}
