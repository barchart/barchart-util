package com.barchart.util.value.impl;

import com.barchart.util.value.api.Price;

public class TestAsDouble {
	
	public static void main(final String[] args) {
		
		Price price = ValueConst.ZERO_PRICE;
		
		price.add(100);
		
		price.div(25);
		
		System.out.println(price.asDouble());
		
		
	}

}
