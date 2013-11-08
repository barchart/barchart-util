/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
/**
 * 
 */
package temp;

import com.barchart.util.math.MathExtra;
import com.barchart.util.value.api.Scaled;

/**
 * Utility class for deriving primitive representations of Value objects.
 * 
 */
public class ValueUtil {

	private ValueUtil() {

	}

	/**
	 * 
	 * @param decimal
	 * @return
	 * @throws ArithmeticException
	 */
	public static long asLong(final Scaled<?> decimal)
			throws ArithmeticException {

		long m = decimal.mantissa();
		int e = decimal.exponent();

		while (e > 0) {
			m = MathExtra.longMult10(m);
			e--;
		}
		while (e < 0) {
			m /= 10;
			e++;
		}

		return m;

	}

	/**
	 * 
	 * @param sd
	 * @return
	 * @throws ArithmeticException
	 */
	public static int asInt(final Scaled<?> sd) throws ArithmeticException {

		return MathExtra.castLongToInt(asLong(sd));

	}

	/**
	 * 
	 * @param sd
	 * @return
	 * @throws ArithmeticException
	 */
	public static double asDouble(final Scaled<?> sd)
			throws ArithmeticException {

		return sd.mantissa() * Math.pow(10, sd.exponent());

	}

	/**
	 * 
	 * @param sd
	 * @return
	 * @throws ArithmeticException
	 */
	public static float asFloat(final Scaled<?> sd)
			throws ArithmeticException {

		return (float) asDouble(sd);

	}

}
