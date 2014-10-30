package com.barchart.util.guice;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;

import com.barchart.util.guice.component.Configurator;

public class ComponentTest {

	private static final class Main {
		
		@Inject
//		@Named("component1")
		private MyComponent component1;
		
		@Inject
//		@Named("component2")
		private MyComponent component2;
		
//		@Inject
		private List<MyComponent> components;

		@Override
		public String toString() {
			return "Main [component1=" + component1 + ", component2=" + component2 + ", components=" + components + "]";
		}
		
		
		
	}
	
	private static final class MyComponent {
		
		@Inject
		@Named("name")
		private String name;

		@Override
		public String toString() {
			return "MyComponent [name=" + name + "]";
		}
		
		
		
	}
	
	
	@Test
	public void test() {
		Configurator configurator = new Configurator(new File("src/test/resources/components"));
		Main main = configurator.getInstance(Main.class);
		System.out.println("Main:\n" + main);
	}
}
