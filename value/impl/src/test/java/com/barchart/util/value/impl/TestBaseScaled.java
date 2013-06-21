package com.barchart.util.value.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Price;

public class TestBaseScaled {
	
	private static final long TIMEOUT_MILLIS = 250;
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testDiv() {
		Price price = ValueConst.ZERO_PRICE;
		price.div(25);
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testScaledDiv() {

		Price price = ValueConst.ZERO_PRICE;
		Decimal denom = ValueBuilder.newDecimal(1, 0);
		price.div(denom);
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testAddLong() {
		
		Decimal answer = ValueBuilder.newDecimal(2, 0);
		Decimal decimal = ValueBuilder.newDecimalMutable(1, 0);
		decimal.add(1);
		
		assertTrue(decimal.equals(answer));

		answer = ValueBuilder.newDecimal(11, -1);
		decimal = ValueBuilder.newDecimalMutable(1, -1);
		decimal.add(1);
		
		assertTrue(decimal.equals(answer));
		
		answer = ValueBuilder.newDecimal(11, 0);
		decimal = ValueBuilder.newDecimalMutable(1, 1);
		decimal.add(1);
		
		assertTrue(decimal.equals(answer));
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testSubLong() {
		
		Decimal answer = ValueBuilder.newDecimal(1, 0);
		Decimal decimal = ValueBuilder.newDecimalMutable(2, 0);
		decimal.sub(1);
		
		assertTrue(decimal.equals(answer));
		
		answer = ValueBuilder.newDecimal(-9, -1);
		decimal = ValueBuilder.newDecimalMutable(1, -1);
		
		decimal.sub(1);
		
		assertTrue(decimal.equals(answer));
		
		answer = ValueBuilder.newDecimal(9, 0);
		decimal = ValueBuilder.newDecimalMutable(1, 1);
		decimal.sub(1);
		
		assertTrue(decimal.equals(answer));
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testGreaterThan() {
		
		/* Not equals */
		Decimal bigger = ValueBuilder.newDecimal(1, 0);
		Decimal smaller = ValueBuilder.newDecimal(1, 0);
		assertFalse(bigger.greaterThan(smaller));
		
		bigger = ValueBuilder.newDecimal(1, 0);
		smaller = ValueBuilder.newDecimal(0, 0);
		assertTrue(bigger.greaterThan(smaller));
		
		bigger = ValueBuilder.newDecimal(1, 0);
		smaller = ValueBuilder.newDecimal(5, -1);
		assertTrue(bigger.greaterThan(smaller));
		
		bigger = ValueBuilder.newDecimal(0, 0);
		smaller = ValueBuilder.newDecimal(-1, 0);
		assertTrue(bigger.greaterThan(smaller));

		bigger = ValueBuilder.newDecimal(-5, -1);
		smaller = ValueBuilder.newDecimal(-1, 0);
		assertTrue(bigger.greaterThan(smaller));
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testLessThan() {
		
		/* Not equals */
		Decimal bigger = ValueBuilder.newDecimal(1, 0);
		Decimal smaller = ValueBuilder.newDecimal(1, 0);
		assertFalse(smaller.lessThan(bigger));
		
		bigger = ValueBuilder.newDecimal(1, 0);
		smaller = ValueBuilder.newDecimal(0, 0);
		assertTrue(smaller.lessThan(bigger));
		
		bigger = ValueBuilder.newDecimal(1, 0);
		smaller = ValueBuilder.newDecimal(5, -1);
		assertTrue(smaller.lessThan(bigger));
		
		bigger = ValueBuilder.newDecimal(0, 0);
		smaller = ValueBuilder.newDecimal(-1, 0);
		assertTrue(smaller.lessThan(bigger));

		bigger = ValueBuilder.newDecimal(-5, -1);
		smaller = ValueBuilder.newDecimal(-1, 0);
		assertTrue(smaller.lessThan(bigger));
		
	}

	@Test(timeout = TIMEOUT_MILLIS)
	public void testAsDouble() {
		
		Price price = ValueBuilder.newPriceMutable(-111, -1);
		
		double doubFromString = Double.parseDouble(price.toString());
		
		assertTrue(Double.compare(doubFromString, price.asDouble()) == 0);
		
	}
	
}
