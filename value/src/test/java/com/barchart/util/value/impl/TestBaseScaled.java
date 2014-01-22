package com.barchart.util.value.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;

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
	
	@Test
	public void testCompareTo() {
		
		Price p1 = ValueBuilder.newPrice(35, 0);
		Price p2 = ValueBuilder.newPrice(3, 1);
		
		assertFalse(p1.equals(p2));
		assertFalse(p2.compareTo(p1) > 0);
		assertTrue(p1.compareTo(p2) > 0);
		
		p1 = ValueBuilder.newPrice(35, -1);
		p2 = ValueBuilder.newPrice(3, 0);
		
		assertFalse(p1.equals(p2));
		assertFalse(p2.compareTo(p1) > 0);
		assertTrue(p1.compareTo(p2) > 0);
		
	}
	
	@Test
	public void testAbs() {
		
		Decimal d = ValueBuilder.newDecimal(1, 0);
		assertTrue(d.equals(d.abs()));
		
		Decimal d2 = ValueBuilder.newDecimal(-1, 0);
		assertTrue(d.equals(d2.abs()));
		
		Price p1 = ValueBuilder.newPrice(5, 1);
		assertTrue(p1.equals(p1.abs()));
		
		Price p2 = ValueBuilder.newPrice(-5, 1);
		assertTrue(p1.equals(p2.abs()));
		
		Size s1 = ValueBuilder.newSize(11, -1);
		assertTrue(s1.equals(s1.abs()));
		
		Size s2 = ValueBuilder.newSize(-11, -1);
		assertTrue(s1.equals(s2.abs()));
		
	}
	
}
