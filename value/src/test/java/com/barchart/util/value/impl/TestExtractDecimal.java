package com.barchart.util.value.impl;

public class TestExtractDecimal {

	public static void main(final String[] args) {
		
		double d = 0;
		long[] res = new long[100];
		int counter = 0;
		int len = 0;
		
		for(long l = Long.MIN_VALUE; l < Long.MAX_VALUE; l++) {
			
			d = Double.longBitsToDouble(l);
			
			try {
			
				DoubleParts parts = MathIEEE754.extractDecimal(d);
				
				res[counter] = parts.getMantissa();
				
				counter++;
				counter %= 100;
				
				
				len++;
				
				if(len % 1000000 == 0) {
					System.out.println("Million");
					len = 0;
				}
				
			} catch (Exception e) {
				
				System.out.println("Conversion failed for " + l);
				
			}
			
		}
		
		
	}
	
}
