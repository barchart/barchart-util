package com.barchart.util.value.impl;

import com.barchart.util.value.api.Price;

public class TestAsDouble {
	
	/*
	 * DELTE ME
	 */
	
	public static void main(final String[] args) {
		
		Price price = ValueBuilder.newPriceMutable(-111, -1);
		
		System.out.println(price);
		System.out.println(price.asDouble());
		
	}

}
