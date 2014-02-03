package com.barchart.util.value.api;

import java.util.Date;
import java.util.TimeZone;

/**
 * Value factory. All values, all the time.
 * 
 */
// NOTE: Update implementation META-INF/service when moving this class.
public interface ValueFactory {

	/* ***** ***** Decimals ***** ***** */
	Decimal newDecimal(long mantissa, int exponent);

	/* ***** ***** Prices ***** ***** */
	Price newPrice(long mantissa, int exponent);
	
	Price newPrice(double price);

	/* ***** ***** Sizes ***** ***** */
	Size newSize(long mantissa, int exponent);

	Size newSize(long size);

	/* ***** ***** Fractions ***** ***** */
	Fraction newFraction(int base, int exponent);

	/* ***** ***** Times ***** ***** */
	Time newTime(long UTCmillis);

	Time newTime(long UTCmillis, TimeZone zone);

	Time newTime(long UTCmillis, String zone);

	Time newTime(Date date);

	Time newTime(Date date, TimeZone zone);

	Time newTime(Date date, String zone);

	/* ***** ***** Time Intervals ***** ***** */
	TimeInterval newTimeInterval(long beginMill, long endMill);

	/* ***** ***** Schedules ***** ***** */
	Schedule newSchedule(TimeInterval[] intervals);

	/* DELTE ME */
	Bool newBoolean(boolean value);

}
