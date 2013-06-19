package com.barchart.util.value.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.barchart.util.value.api.Decimal;

public class TestBaseScaled {
	
	private static final long TIMEOUT_MILLIS = 250;
	
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
		
		/*
		 * TODO
		 */
		assertTrue(false);
		
	}
	
	@Test(timeout = TIMEOUT_MILLIS)
	public void testLessThan() {
		
		/*
		 * TODO
		 */
		assertTrue(false);
		
	}

	@Test(timeout = TIMEOUT_MILLIS)
	public void testAsDouble() {
		
		/*
		 * TODO not formatted correctly
		 */
		assertTrue(false);
		
	}
	
}
