package com.barchart.util.guice;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

public class CustomModuleTest extends InjectorTest {

	private static final Logger logger = LoggerFactory.getLogger(CustomModuleTest.class);

	@Before
	public void setup() throws Exception {
		super.setup("src/test/resources/CustomModuleTest");
	}

	@Test
	public void test() {
		MyComponent myComponent1 = get(MyComponent.class, Names.named("component1"));
		MyComponent myComponent2 = get(MyComponent.class, Names.named("component2"));

		logger.info("MyComponent1: " + myComponent1);
	}

	@Component(value = "ComplexValueConverterTest.Component", customModule = MyCustomModule.class)
	private static final class MyComponent {

		@Inject
		@Named("#name")
		public String name;

		@Inject
		public Set<CustomObject> customObjects;

		@Override
		public String toString() {
			return "MyComponent [name=" + name + ", customObjects=" + customObjects + "]";
		}

	}

	public static final class CustomId {

	}

	public static final class CustomObject {

		@Inject
		public CustomId customId;

		@Override
		public String toString() {
			return "CustomObject [customId=" + customId + "]";
		}

	}

	public static final class MyCustomModule extends AbstractModule {

		@Inject
		@Named("#name")
		private String name;
		
		@Inject
		@Named("#custom_value_list")
		private List<Integer> customValueList;
		
		MyCustomModule() {
		}

		@Override
		protected void configure() {
			logger.info("Custom module!: " + name);
			Multibinder<CustomObject> setBinder = Multibinder.newSetBinder(binder(), CustomObject.class);
			for (int value : customValueList) {
				CustomObject customObject = new CustomObject();
				customObject.customId = new CustomId();
				setBinder.addBinding().toInstance(customObject);
			}

			//
		}

	}

}
