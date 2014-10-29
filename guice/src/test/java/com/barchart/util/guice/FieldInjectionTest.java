package com.barchart.util.guice;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class FieldInjectionTest {

	@Test
	public void test() {
		Module module = new TestModule();
		Injector injector = Guice.createInjector(module);
		MyClass myClass = injector.getInstance(MyClass.class);
		System.out.println(myClass);
		
	}
	
	
	public static final class TestModule  extends AbstractModule {

		@Override
		protected void configure() {
			bindConstant().annotatedWith(Names.named("number1")).to(12);
			bindConstant().annotatedWith(Names.named("number2")).to(42);
			bindConstant().annotatedWith(Names.named("string1")).to("Hello world");
		}		
	}
	
	public static final class MyClass {
		
		@Inject
		@Named("number1")
		private int number1;
		
		@Inject
		@Named("number2")
		private int number2;
		
		@Inject
		@Named("string1")
		private String string1;

		@Override
		public String toString() {
			return "MyClass [number1=" + number1 + ", number2=" + number2 + ", string1=" + string1 + "]";
		}
		
		
		
	}
}
