package com.barchart.util.guice;

import javax.inject.Inject;
import javax.inject.Singleton;

public class Common {

	@Component("ComponentScopeTest.component")
	public static class TestComponent implements IFace {

		@Inject
		public SharedDependency sharedDep;

		@Inject
		public ComponentScopedDependency componentDep;

	}

	public interface IFace {

	}

	@Singleton
	public static class SharedDependency {

	}

	@ComponentScoped
	public static class ComponentScopedDependency {

	}
}
