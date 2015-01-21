package com.barchart.util.guice;

import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class ValueSubstitutionTest {

	@Test
	public void test() throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append("realvalue = a").append("\n");
		builder.append("sub = ${realvalue}").append("\n");
		String confString = builder.toString();
		Injector injector = new GuiceConfigBuilder(true).setConfigResources(new StringResources(confString)).build();
		Assert.assertEquals("a",  injector.getInstance(Key.get(String.class, Names.named("sub"))));
	}
	
}
