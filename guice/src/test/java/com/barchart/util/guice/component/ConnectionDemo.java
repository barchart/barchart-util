package com.barchart.util.guice.component;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import com.barchart.util.guice.Component;
import com.barchart.util.guice.GuiceConfigBuilder;
import com.google.inject.Injector;
import com.typesafe.config.Config;

public class ConnectionDemo {

	/**
	 * Configuration fields are bound by the config path. application.conf is
	 * the default file, so no file name is needed.
	 */
	@Inject
	@Named("application_id")
	private String myApplicationId;

	/**
	 * Configuration files (.conf) other than application.conf can be referenced
	 * by filename followed by a slash. The file extension is dropped because
	 * the dot can get confused for the Hocon object separator.
	 */
	@Inject
	@Named("external/text")
	private String externalText;

	/**
	 * Hocon objects are bound as Config objects themselves and are referenced
	 * just like any other value.
	 */
	@Inject
	@Named("some_configuration_object")
	private Config config;

	/**
	 * Configuration values inside nested objects can be referenced as expected.
	 */
	@Inject
	@Named("some_other_configuration_object.ignore")
	private boolean isIngore;

	/**
	 * Configuration objects that exists in the application.conf/component list
	 * or in .component files, are treated specially as component objects. Each
	 * component configuration requires a "type" field, which matches a @Component
	 * annotation on class. If more than one component configuration exists for
	 * the same @Component, then a unique "name" field is required.
	 */
	@Inject
	@Named("feed1")
	private Connection feed1;

	/**
	 * If you know the names of the components at compile time, you can inject
	 * by name.
	 */
	@Inject
	@Named("feed2")
	private Connection feed2;

	/**
	 * For the cases where you don't know the names of all the components you
	 * are interested in, you can have a list of all components of a given type
	 * injected. We could also inject a name to instance map as well, if we
	 * want.
	 */
	@Inject
	private List<Connection> allConnections;

	ConnectionDemo() {

	}

	/**
	 * Any class that guice can inject can be annotated as a @Component. (We
	 * could consider reusing javax.inject.Named annotation instead of a new
	 * annotation, but that might be confusing)
	 */
	@Component("barchart.connection")
	public static final class Connection {

		/**
		 * Special syntax is used to reference the fields in a component's
		 * configuration. '#' is used to reference this component's
		 * configuration object.
		 */
		@Inject
		@Named("#")
		private Config config;

		/**
		 * The fields in this component's configuration are bound with a
		 * prefixed '#'
		 */
		@Inject
		@Named("#host")
		private String host;

		@Inject
		@Named("#port")
		private int port;

		/**
		 * The special syntax lets the component reference the static
		 * (non-component) configuration without having naming conflicts
		 */
		@Inject
		@Named("application_id")
		private String myApplicationId;

	}

	public static void main(String[] args) {
		Injector injector = GuiceConfigBuilder.create() //
				.setDirectory("./src/test/resources/connectiondemo") //
				.build();
		ConnectionDemo demo = injector.getInstance(ConnectionDemo.class);
		System.out.println(demo);
	}
}
