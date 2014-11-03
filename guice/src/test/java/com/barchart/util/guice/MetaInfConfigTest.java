//package com.barchart.util.guice;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.Enumeration;
//import java.util.Properties;
//import java.util.Set;
//
//import org.junit.Test;
//
//import com.google.common.reflect.ClassPath;
//import com.google.common.reflect.ClassPath.ResourceInfo;
//import com.google.inject.Injector;
//
//public class MetaInfConfigTest {
//
//	
//	@Test
//	public void readConfig() throws IOException {
//		Properties properties = System.getProperties();
//		Set<Object> keySet = properties.keySet();
//		for (Object key :  keySet) {
//			System.out.println(key + " \t=\t " + properties.getProperty((String) key));
//			
//		}
//		ClassPath classpath = ClassPath.from(this.getClass().getClassLoader());
////		for (ResourceInfo resourceInfo : classpath.getResources()) {
////			System.out.println("Info: " + resourceInfo.url());
//		Injector injector = GuiceConfigBuilder.create() //
//				
//			.build();
//			
////		}
//	}
//	
//}
