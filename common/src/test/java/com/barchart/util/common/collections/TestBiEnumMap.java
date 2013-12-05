package com.barchart.util.common.collections;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestBiEnumMap {

	 private enum TEST1 {
         T1A, T1B, T1C, T1D, T1E
 }
 
 private enum TEST2 {
         T2A, T2B, T2C, T2D, T2E
 }
 
 @Test
 public void testBiEnumMap() {
         
         final BiEnumMap<TEST1, TEST2> test1 = new BiEnumMap<TEST1, TEST2>(
                         new TEST1[] {TEST1.T1A, TEST1.T1B, TEST1.T1C, TEST1.T1D, TEST1.T1E},
                         new TEST2[] {TEST2.T2A, TEST2.T2B, TEST2.T2C, TEST2.T2D, TEST2.T2E}
                         );
         
         assertTrue(test1.getValue(TEST1.T1A) == TEST2.T2A);
         assertTrue(test1.getValue(TEST1.T1C) == TEST2.T2C);
         assertTrue(test1.getValue(TEST1.T1E) == TEST2.T2E);
         assertTrue(test1.getKey(TEST2.T2A) == TEST1.T1A);
         assertTrue(test1.getKey(TEST2.T2C) == TEST1.T1C);
         assertTrue(test1.getKey(TEST2.T2E) == TEST1.T1E);
         
         final BiEnumMap<TEST1, TEST2> test2 = new BiEnumMap<TEST1, TEST2>(
                         new TEST1[] {TEST1.T1A, TEST1.T1C, TEST1.T1E},
                         new TEST2[] {TEST2.T2A, TEST2.T2C, TEST2.T2E}
                         );
         
         assertTrue(test2.getValue(TEST1.T1A) == TEST2.T2A);
         assertTrue(test2.getValue(TEST1.T1C) == TEST2.T2C);
         assertTrue(test2.getValue(TEST1.T1E) == TEST2.T2E);
         assertTrue(test2.getKey(TEST2.T2A) == TEST1.T1A);
         assertTrue(test2.getKey(TEST2.T2C) == TEST1.T1C);
         assertTrue(test2.getKey(TEST2.T2E) == TEST1.T1E);
         
         final BiEnumMap<TEST1, TEST2> test3 = new BiEnumMap<TEST1, TEST2>(
                         new TEST1[] {TEST1.T1A, TEST1.T1B, TEST1.T1C, TEST1.T1D, TEST1.T1E},
                         new TEST2[] {TEST2.T2E, TEST2.T2A, TEST2.T2B, TEST2.T2C, TEST2.T2D}
                         );
         
         assertTrue(test3.getValue(TEST1.T1A) == TEST2.T2E);
         assertTrue(test3.getValue(TEST1.T1C) == TEST2.T2B);
         assertTrue(test3.getValue(TEST1.T1E) == TEST2.T2D);
         assertTrue(test3.getKey(TEST2.T2A) == TEST1.T1B);
         assertTrue(test3.getKey(TEST2.T2C) == TEST1.T1D);
         assertTrue(test3.getKey(TEST2.T2E) == TEST1.T1A);
         
 }
 
	
}
