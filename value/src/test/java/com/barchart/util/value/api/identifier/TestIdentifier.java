package com.barchart.util.value.api.identifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestIdentifier {

	
	@Test
	public void testID() {
		
		final TestId id1 = new TestId("Test1");
		final TestId id2 = new TestId("Test1");
		final TestId id3 = new TestId("Test3");
		
		assertTrue(id1.equals(id2));
		assertTrue(id1.hashCode() == id2.hashCode());
		assertTrue(id1.compareTo(id2) == 0);
		assertTrue(id1.toString().equals(id2.toString()));
		
		assertFalse(id1.equals(id3));
		assertFalse(id1.hashCode() == id3.hashCode());
		assertTrue(id1.compareTo(id3) < 0);
		assertFalse(id1.toString().equals(id3.toString()));
		
	}
	
}
