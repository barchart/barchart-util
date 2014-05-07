package com.barchart.util.value.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Decimal;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.ValueFactory;

public class TestBaseScaled {
	
	public static final ValueFactory vals = ValueFactoryImpl.instance;

	private static final long TIMEOUT_MILLIS = 250;
	
	@Test
	public void testRound() {
		
		Price price = vals.newPrice(123456, -4);
		assertEquals(vals.newPrice(1235, -2), price.round(2));
		
		price = vals.newPrice(1234567890, -8);
		assertEquals(vals.newPrice(1235, -2), price.round(2));
		
		price = vals.newPrice(12345, 1);
		assertEquals(vals.newPrice(12345, 1), price.round(100));
		
		try {
			price.round(-1);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (Exception ex) {
			assertTrue(false);
		}
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testDiv() {
		Price price = Price.ZERO;
		price.div(25);
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testScaledDiv() {

		Price price = Price.ZERO;
		Decimal denom = vals.newDecimal(1, 0);
		price.div(denom);
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testAddLong() {
		
		Decimal answer = vals.newDecimal(2, 0);
		Decimal decimal = vals.newDecimal(1, 0);
		decimal = decimal.add(1);
		
		assertTrue(decimal.equals(answer));

		answer = vals.newDecimal(11, -1);
		decimal = vals.newDecimal(1, -1);
		decimal = decimal.add(1);
		
		assertTrue(decimal.equals(answer));
		
		answer = vals.newDecimal(11, 0);
		decimal = vals.newDecimal(1, 1);
		decimal = decimal.add(1);
		
		assertTrue(decimal.equals(answer));
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testSubLong() {
		
		Decimal answer = vals.newDecimal(1, 0);
		Decimal decimal = vals.newDecimal(2, 0);
		decimal = decimal.sub(1);
		
		assertTrue(decimal.equals(answer));
		
		answer = vals.newDecimal(-9, -1);
		decimal = vals.newDecimal(1, -1);
		
		decimal = decimal.sub(1);
		
		assertTrue(decimal.equals(answer));
		
		answer = vals.newDecimal(9, 0);
		decimal = vals.newDecimal(1, 1);
		decimal = decimal.sub(1);
		
		assertTrue(decimal.equals(answer));
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testGreaterThan() {
		
		/* Not equals */
		Decimal bigger = vals.newDecimal(1, 0);
		Decimal smaller = vals.newDecimal(1, 0);
		assertFalse(bigger.greaterThan(smaller));
		
		bigger = vals.newDecimal(1, 0);
		smaller = vals.newDecimal(0, 0);
		assertTrue(bigger.greaterThan(smaller));
		
		bigger = vals.newDecimal(1, 0);
		smaller = vals.newDecimal(5, -1);
		assertTrue(bigger.greaterThan(smaller));
		
		bigger = vals.newDecimal(0, 0);
		smaller = vals.newDecimal(-1, 0);
		assertTrue(bigger.greaterThan(smaller));

		bigger = vals.newDecimal(-5, -1);
		smaller = vals.newDecimal(-1, 0);
		assertTrue(bigger.greaterThan(smaller));
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testLessThan() {
		
		/* Not equals */
		Decimal bigger = vals.newDecimal(1, 0);
		Decimal smaller = vals.newDecimal(1, 0);
		assertFalse(smaller.lessThan(bigger));
		
		bigger = vals.newDecimal(1, 0);
		smaller = vals.newDecimal(0, 0);
		assertTrue(smaller.lessThan(bigger));
		
		bigger = vals.newDecimal(1, 0);
		smaller = vals.newDecimal(5, -1);
		assertTrue(smaller.lessThan(bigger));
		
		bigger = vals.newDecimal(0, 0);
		smaller = vals.newDecimal(-1, 0);
		assertTrue(smaller.lessThan(bigger));

		bigger = vals.newDecimal(-5, -1);
		smaller = vals.newDecimal(-1, 0);
		assertTrue(smaller.lessThan(bigger));
		
	}

	@Test(timeout = TIMEOUT_MILLIS)
	public void testAsDouble() {
		
		Price price = vals.newPrice(-111, -1);
		
		double doubFromString = Double.parseDouble(price.toString());
		
		assertTrue(Double.compare(doubFromString, price.asDouble()) == 0);
		
	}
	
	@Test
	public void testCompareTo() {
		
		Price p1 = vals.newPrice(35, 0);
		Price p2 = vals.newPrice(3, 1);
		
		assertFalse(p1.equals(p2));
		assertFalse(p2.compareTo(p1) > 0);
		assertTrue(p1.compareTo(p2) > 0);
		
		p1 = vals.newPrice(35, -1);
		p2 = vals.newPrice(3, 0);
		
		assertFalse(p1.equals(p2));
		assertFalse(p2.compareTo(p1) > 0);
		assertTrue(p1.compareTo(p2) > 0);
		
		p1 = vals.newPrice(552.391686);
		p2 = vals.newPrice(555.750);
		
		assertTrue(p1.compareTo(p2) < 0);
		assertTrue(p2.compareTo(p1) > 0);

	}
	
	@Test
	public void testAbs() {
		
		Decimal d = vals.newDecimal(1, 0);
		assertTrue(d.equals(d.abs()));
		
		Decimal d2 = vals.newDecimal(-1, 0);
		assertTrue(d.equals(d2.abs()));
		
		Price p1 = vals.newPrice(5, 1);
		assertTrue(p1.equals(p1.abs()));
		
		Price p2 = vals.newPrice(-5, 1);
		assertTrue(p1.equals(p2.abs()));
		
		Size s1 = vals.newSize(11, -1);
		assertTrue(s1.equals(s1.abs()));
		
		Size s2 = vals.newSize(-11, -1);
		assertTrue(s1.equals(s2.abs()));
		
	}
	
}
