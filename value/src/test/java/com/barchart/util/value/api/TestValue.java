package com.barchart.util.value.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.barchart.util.value.ValueFactoryImpl;

public class TestValue {
	
	public static final ValueFactory vals = new ValueFactoryImpl();

	@Test
	public void testPriceMin() {
		
		Price min = vals.newPrice(-1, 5);
		assertTrue(Value.min(null, Price.NULL, min, vals.newPrice(-1, 4), vals.newPrice(0, 0)).equals(min));
		assertTrue(Value.min(null, Price.NULL).equals(Price.NULL));
		
	}
	
	@Test
	public void testPriceMax() {
		
		Price max = vals.newPrice(1, 5);
		assertTrue(Value.max(null, Price.NULL, max, vals.newPrice(1, 4), vals.newPrice(0, 0)).equals(max));
		assertTrue(Value.max(null, Price.NULL).equals(Price.NULL));
		
	}
	
	@Test
	public void testSizeMin() {
		
		Size min = vals.newSize(-1, 5);
		assertTrue(Value.min(null, Size.NULL, min, vals.newSize(-1, 4), vals.newSize(0, 0)).equals(min));
		assertTrue(Value.min(null, Size.NULL).equals(Size.NULL));
		
	}
	
	@Test
	public void testSizeMax() {
		
		Size max = vals.newSize(1, 5);
		assertTrue(Value.max(null, Size.NULL, max, vals.newSize(1, 4), vals.newSize(0, 0)).equals(max));
		assertTrue(Value.max(null, Size.NULL).equals(Size.NULL));
		
	}
	
}
