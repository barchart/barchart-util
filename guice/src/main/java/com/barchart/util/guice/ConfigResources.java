package com.barchart.util.guice;

import java.util.List;

public interface ConfigResources {

	public String readResource(String resourceName);
	
	public List<String> listResources();
	
}
