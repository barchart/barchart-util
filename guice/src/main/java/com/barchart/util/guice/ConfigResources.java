package com.barchart.util.guice;

import java.util.List;

import com.typesafe.config.Config;

public interface ConfigResources {

	public String readResource(String resourceName) throws Exception;

	public Config readConfig(String resourceName) throws Exception;
	
	public List<String> listResources();

	/**
	 * 
	 * @return a string describing the path of these resources, such as a
	 *         directory name or a classpath URL
	 */
	public String getPathDescription();

	

}
