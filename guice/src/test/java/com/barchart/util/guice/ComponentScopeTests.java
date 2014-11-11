package com.barchart.util.guice;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import com.google.inject.name.Names;

@RunWith(Enclosed.class)
public class ComponentScopeTests {

	

//	/*
//	 * @ComponentScoped dependencies with no qualifiers are scoped to the nearest parent component 
//	 *
//	 *	[Component1]
//	 *        |
//	 *        |-----------
//	 *        |          |
//	 *       Dep1    [Component2]
//	 *                   |
//	 *                  Dep2
//	 *                  
//	 * Dep1 and Dep2 are different                 
//	 */
//	public static class TestCase2 extends InjectorTest {
//	
//	}
//	
//	
//	/*
//	 *
//	 * @ComponentScoped dependencies 
//	 * 
//	 *    [Component1]
//	 *         |
//	 *         |----------------- Dep1
//	 *         |
//	 *  IntermediateDep (Not a singleton)
//	 *         |
//	 *        Dep1     
//	 *       
//	 * 
//	 * TODO: If IntermediateDep is a singleton, this might introduce problems
//	 */
//	public static class TestCase3 extends InjectorTest {
//		
//	}
//	
//	
//	/*
//	 * @ComponentScoped dependency, tied to component type
//	 * 
//	 * [Component1, type=A]     [Component2, type=A]   [Component3, type=A]   [Component4, type=B]
//	 *          |                          |                     |                       |
//	 *          |                          |                     |                       |
//	 *        Dep1                       Dep1                   Dep1                    Dep2
//	 * 
//	 */
//	public static class TestCase4 extends InjectorTest {
//		
//		@Before 
//		public void setup() throws Exception {
//			super.setup("src/test/resources/ComponentScopeTest/TestCase4");
//		}
//		
//		@Test
//		public void test() {
//			ComponentA component1 = get(ComponentA.class, Names.named("comp1"));
//			ComponentA component2 = get(ComponentA.class, Names.named("comp2"));
//			ComponentA component3 = get(ComponentA.class, Names.named("comp3"));
//			ComponentB component4 = get(ComponentB.class, Names.named("comp4"));
//			
//		}
//		
//
//
//		private static final class Component1 {
//			
//		}
//		
//		private static final class ComponentA {
//			
//		}
//		
//		private static final class ComponentB {
//			
//		}
//		
//		
//		private static final class Dependency {
//			
//		}
//		
//	}
//	
//	/*
//	 * @ComponentScoped dependency, tied to component name
//	 * 
//	 * [Component1, type=A, name=comp3]     [Component2, type=A, name=comp2]   [Component3, type=A, name=comp3]
//	 *                 |                                    |                                  |         
//	 *                 |                                    |                                  |         
//	 *                Dep1                                 Dep2                               Dep3       
//	 * 
//	 */
//	public static class TestCase5 extends InjectorTest {
//		
//	}

}
